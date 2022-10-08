$(document).ready(function () {



    $(document).on('click', '.modal-dismiss', function(e) {
            e.preventDefault();
            $.magnificPopup.close();
        }
    );
    $('.modal-basic').magnificPopup( {
            type: 'inline', preloader: false, modal: true
        }
    );
    $('.modal-with-move-anim').magnificPopup( {
            type: 'inline', fixedContentPos: false, fixedBgPos: true, overflowY: 'auto', closeBtnInside: true, preloader: false, midClick: true, removalDelay: 300, mainClass: 'my-mfp-slide-bottom', modal: true
        }
    );
    $('.modal-with-form').magnificPopup( {
            type:'inline', preloader:false, focus:'#name', modal:true, callbacks: {
                beforeOpen:function() {
                    if($(window).width()<700) {
                        this.st.focus=false;
                    }
                    else {
                        this.st.focus='#name';
                    }
                }
            }
        }
    );
    $('.simple-ajax-modal').magnificPopup( {
            type: 'ajax', modal: true,
        }
    );

    function displayMessage(data) {
        if(data['errors'])
        {
            var d = 0;
            for(var titre in data['errors'])
            {
                d=d+2;
                var errors = data['errors'][titre];
                for (var i in errors)
                {
                    setTimeout(()=>{
                        var notice=new PNotify( {
                                title: 'Notification', text: titre+" - "+ errors[i], type: 'error', addclass: 'stack-topright',delay : 7000
                            }
                        );
                    }, (d*1000)/2);

                }
            }
        }
        if(data['success'])
        {
            for(var i in data['success'])
            {
                var succes = data['success'][i];
                var notice=new PNotify( {
                        title: 'Notification', text: " - "+ succes, type: 'success', addclass: 'stack-topright',delay : 5000
                    }
                );
            }
        }
    }
});