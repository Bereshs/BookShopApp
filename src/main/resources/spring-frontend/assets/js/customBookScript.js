$(document).ready(function(){
    $('a.dwn').click(function(event){
        event.preventDefault();
        $('#myOverlay').fadeIn(279, function(){
            $('#myPopup').css('display', 'block').animate({opacity:1}, 198);
        })
    })
    $('#myPopup__close, #myOverlay').click(function(){
        $('#myPopup').animate({opacity:1},198, function() {
            $(this).css('display','none');
            $('#myOverlay').fadeOut(297);
        })
    })

    $('a.btn_review').click(function(event){
        event.preventDefault();
        $('#myOverlay').fadeIn(279, function(){
            $('#reviewPopup').css('display', 'block').animate({opacity:1}, 198);
        })
    })
    $('#myPopup__close, #myOverlay').click(function(){
        $('#reviewPopup').animate({opacity:1},198, function() {
            $(this).css('display','none');
            $('#myOverlay').fadeOut(297);
        })
    })
    $('button.btn_reviewsend').click(function(event){
    var $this=$(this), data={};
        data.bookId = $this.data('bookid');
        data.text =  $('.reviewText').val();
        customPostData('/api/bookReview', data, function (result) {
             $('#myPopup__close').click();
             });
})

function customPostData(address, data, cb) {
                        $.ajax({
                            url:  address,
                            type: 'POST',
                            dataType: 'json',
                            data: data,
                            complete: function (result) {
                                if (result.status === 200) {
                                    cb(result.responseJSON);
                                } else {
                                    alert('Ошибка ' + result.status);
                               //     if (cbErr) {
                                //        cbErr(result.responseJSON);
                                //    }
                                }
                            }
                        });
                    }

    })

