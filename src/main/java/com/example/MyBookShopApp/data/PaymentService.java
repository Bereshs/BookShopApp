package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.BalanceTransactionRepository;
import com.example.MyBookShopApp.data.storage.UserRepository;
import com.example.MyBookShopApp.struct.book.BookEntity;
import com.example.MyBookShopApp.struct.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.struct.payments.TransactionStatus;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class PaymentService {

    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String firstTestPass;

    @Value("${bookshop.is.testing.mode}")
    private boolean isTestingMode;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final UserRepository userRepository;


    @Autowired
    public PaymentService(BalanceTransactionRepository balanceTransactionRepository, UserRepository userRepository) {
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.userRepository = userRepository;
    }


    public RedirectView createBankTransaction(Integer sum, UserEntity user, String cartContents) throws URISyntaxException, NoSuchAlgorithmException {
        if (isTestingMode) {
            createFakeRequestTransaction(sum, user, cartContents);
            return new RedirectView("http://localhost:8085/payment");
        }
        String paymentUrl = getPaymentUrl(sum);
        return new RedirectView(paymentUrl);
    }

    public String getPaymentUrl(double paymentSumTotal) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5"; //just for testing TODO order indexing later
        md.update((merchantLogin + ":" + Double.toString(paymentSumTotal) + ":" + invId + ":" + firstTestPass).getBytes());
        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&IndId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + Double.toString(paymentSumTotal) +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    public String[] getCartContents(String cartContents) {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) :
                cartContents;

        return cartContents.split("/");
    }

    public void createFakeRequestTransaction(Integer sum, UserEntity user, String cartContents) throws URISyntaxException {
        String hash = createTransaction(sum, user, user.getHash(), 0, cartContents);
        RestTemplate template = new RestTemplate();
        URI uri = new URI("http://localhost:8085/api/payment?hash=" + hash + "&sum=" + sum + "&time=" + (System.currentTimeMillis() / 1000));
        String result = template.postForObject(uri, null, String.class);
    }


    public BalanceTransactionEntity getByHash(String hash) {
        return balanceTransactionRepository.getByHash(hash);
    }

    public String createTransaction(Integer sum, UserEntity user, String hash) {
        String description = "Пополнение кошелька";
        int bookId = 0;
        return createTransaction(sum, user, hash, bookId, description);
    }

    public String createTransaction(Integer sum, UserEntity user, String hash, int bookId, String description) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        LocalDateTime utcTime = OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
        transaction.setBookId(bookId);
        transaction.setUserId(user.getId());
        transaction.setValue(sum);
        transaction.setHash(hash + utcTime.toString());
        transaction.setDescription(description);
        transaction.setStatus(TransactionStatus.created);
        transaction.setTime(utcTime);
        balanceTransactionRepository.save(transaction);
        return transaction.getHash();
    }


    public void confirmTransaction(String hash, String description) {
        BalanceTransactionEntity transaction = balanceTransactionRepository.getByHash(hash);
        UserEntity user = userRepository.getById(transaction.getUserId());
        int userBalanceAfterOperation = user.getBalance() + transaction.getValue();
        if (transaction.getHash().equals(hash) && userBalanceAfterOperation > 0) {
            saveTransactionStatus(transaction, TransactionStatus.confirmed, description);
            user.setBalance(userBalanceAfterOperation);
            userRepository.save(user);
        }
    }

    public void confirmTransaction(String hash) {
        confirmTransaction(hash, "");
    }

    public void requestTransaction(String hash) {
        confirmTransaction(hash, "");
    }

    private void saveTransactionStatus(BalanceTransactionEntity transaction, TransactionStatus status, String description) {
        transaction.setStatus(status);
        if (description.length() > 0) {
            transaction.setDescription(description);
        }
        balanceTransactionRepository.save(transaction);
    }

    private void saveTransactionStatus(BalanceTransactionEntity transaction, TransactionStatus status) {
        saveTransactionStatus(transaction, status, "");
    }


    public List<BalanceTransactionEntity> getListByUserId(int userId) {
        return balanceTransactionRepository.findAllByUserId(userId);
    }

    public void createTransactionsFromBookList(List<BookEntity> bookList, UserEntity user) {
        bookList.forEach(bookEntity -> {
            String description = bookEntity.getTitle();
            String hash = createTransaction(bookEntity.getPrice() * (-1), user, user.getHash(), bookEntity.getId(), description);
            confirmTransaction(hash);
        });

    }

}
