document.addEventListener('DOMContentLoaded', function() {
    const btnPresence = $('#abscenceBtn');
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: [ 'interaction','dayGrid', 'timeGrid', 'list' ],
        locale : 'frLocale',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
        },
        defaultDate: new Date(),
        editable: false,
        navLinks: true, // can click day/week names to navigate views
        eventLimit: true, // allow "more" link when too many events
        events: {
            url: '/activities/get_meetings',
            failure: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
            }
        },
        eventClick: function(arg) {
            var id = arg.event.id;
            var type = arg.event.groupId;
            $(document).off('click','.modal-confirm');
            $(document).on('click', '.modal-confirm', function(e) {
                    e.preventDefault();
                    $.magnificPopup.close();
                    deleteActivity(id,type,arg.event);
                    //console.log(arg.event.groupId);
                }
            );
            $(document).off('click','#modifierActivity');
            $(document).on('click', '#modifierActivity', function(e) {
                    e.preventDefault();
                    window.location.assign("/activities/modify_"+type+"/"+id);
                }
            );
            $(document).off('click','.modal-confirm-google');
            $(document).on('click', '.modal-confirm-google', function(e) {
                    e.preventDefault();
                    $.magnificPopup.close();
                    addActivityGoogleCalendar(id,type);
                }
            );
            $(document).off('click','#abscenceBtn');
            $(document).on('click', '#abscenceBtn', function(e) {
                    e.preventDefault();
                    marquer(id);
                }
            );
            /*if (confirm('delete event?')) {
                arg.event.remove()
            }*/
            //console.log(type);
            if(type==="meeting")
                getMeetingById(id);
            else
                getEventById(id);
        },

        loading: function(bool) {
            (bool)?$(".loader").removeClass("hidden") : $(".loader").addClass("hidden");
        }
    });
    calendar.setOption('locale', 'fr');
    calendar.render();

    function marquer(id) {

        $.ajax({
            type: "GET",
            url: "/activities/marquer_presence/"+id,
            dataType: "JSON",
            beforeSend : function () {
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                displayBtnPresence(data['presence']);
                getMeetingById(id);
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('.loader').addClass('hidden');
            }
        });
    }

    function addActivityGoogleCalendar(id,type) {
        const token = $("#token").find('input:text:first').val();
        $.ajax({
            type: "POST",
            url: "/activities/add_Google_Calendar/"+type+"/"+id,
            data : {token:token},
            dataType: "JSON",
            beforeSend : function () {
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                //console.log(data['data']);
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('.loader').addClass('hidden');
            }
        });
    }
    function deleteActivity(id,type,calend) {
        $.ajax({
            type: "POST",
            url: "/activities/delete_activity/"+type+"/"+id,
            dataType: "JSON",
            beforeSend : function () {
                $('#details').parent('div').fadeOut();
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                calend.remove();
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('.loader').addClass('hidden');
            }
        });
    }
    function getEventById(id) {
        var blockDetails = $('#details');
        var blockPartic = $('#participants');
        $(".toHide").addClass('hidden');
        $.ajax({
            type: "GET",
            url: "/activities/get_event_Detail/"+id,
            dataType: "JSON",
            beforeSend : function () {
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                //console.log(data);
                blockDetails.html(data['details']);
                blockDetails.parent('div').fadeIn();
                blockPartic.html(data['participants']);
                $('#modalAnim').attr('data-id',id);

                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('.loader').addClass('hidden');
            }
        });
    }
    function getMeetingById(id) {
        var blockDetails = $('#details');
        var blockPartic = $('#participants');
        $(".toHide").removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/activities/get_meeting_Detail/"+id,
            dataType: "JSON",
            beforeSend : function () {
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                //console.log(data);
                blockDetails.html(data['details']);
                blockDetails.parent('div').fadeIn();
                blockPartic.html(data['participants']);
                displayBtnPresence(data['presence']);
                $('#modalAnim').attr('data-id',id);

                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('.loader').addClass('hidden');
            }
        });
    }

    function displayBtnPresence(presence) {
        btnPresence.hide();
        if (presence == 1)
        {
            btnPresence.removeClass("btn-default");
            btnPresence.addClass("btn-warning");
            btnPresence.text('Marquer absent');
            btnPresence.fadeIn();
        }
        else if(presence == 0)
        {
            btnPresence.removeClass("btn-warning");
            btnPresence.addClass("btn-default");
            btnPresence.text('Marquer present');
            btnPresence.fadeIn();
        }
    }

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