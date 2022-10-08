(function($) {
        'use strict';
        $('.modal-basic').magnificPopup( {
                type: 'inline', preloader: false, modal: true
            }
        );
        $('.modal-sizes').magnificPopup( {
                type: 'inline', preloader: false, modal: true
            }
        );
        $('.modal-with-zoom-anim').magnificPopup( {
                type: 'inline', fixedContentPos: false, fixedBgPos: true, overflowY: 'auto', closeBtnInside: true, preloader: false, midClick: true, removalDelay: 300, mainClass: 'my-mfp-zoom-in', modal: true
            }
        );
        $('.modal-with-move-anim').magnificPopup( {
                type: 'inline', fixedContentPos: false, fixedBgPos: true, overflowY: 'auto', closeBtnInside: true, preloader: false, midClick: true, removalDelay: 300, mainClass: 'my-mfp-slide-bottom', modal: true
            }
        );
        $(document).on('click', '.modal-dismiss', function(e) {
                e.preventDefault();
                $.magnificPopup.close();
            }
        );
        $(document).on('click', '.modal-confirm', function(e) {
                e.preventDefault();
                $.magnificPopup.close();
                new PNotify( {
                        title: 'Success!', text: 'Modal Confirm Message.', type: 'success'
                    }
                );
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
                type: 'ajax', modal: true
            }
        );
    }

).apply(this, [jQuery]);