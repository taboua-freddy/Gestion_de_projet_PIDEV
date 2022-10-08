$(document).ready(function () {
    const divOptions = $('#filter-options');
    const inputNormal = $('.input-normal');
    const inputDateRange = $('.input-date-range');
    const divCloseSearch = $('.close-search');
    const divCalendar = $('#calendar');
    const divSearch = $('#divSearch');
    const importanceOption = $('#importance-filter');
    init();
    $('#display-filter').on('click', function (event) {
        event.preventDefault();
        divOptions.fadeToggle("slow");
    });
    $('#byTitle,#byDate').on('change', function () {
        displayInput();
    });
    $('#byMeeting,#byEvent').on('change',function () {
        displayMeetingOptions();
    });
    $('.btn-close-search').on('click',function (event) {
        event.preventDefault();
        divCloseSearch.fadeOut();
        divCalendar.fadeIn();
        divSearch.fadeOut();
    });
    $('#search').on('click',function (event) {
        event.preventDefault();
        divCloseSearch.fadeIn();
        divSearch.fadeIn();
        divCalendar.fadeOut();
        search();
    });

    function search() {
        form = $('#search-activities');
        $.ajax({
            type: "POST",
            url: form.attr("action"),
            data: form.serialize(),
            dataType: "JSON",
            beforeSend : function () {
                $('#search').addClass('disabled');
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                divSearch.html(data['searchedItems']);

                $('#search').removeClass("disabled");
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('#search').removeClass("disabled");
                $('.loader').addClass('hidden');
            }
        });
    }

    function displayInput() {
        const titleElement = $('#byTitle');
        const dateElement = $('#byDate');
        if (titleElement.prop('checked')) {
            inputDateRange.hide("slow");
            inputNormal.show("slow");
        } else if (dateElement.prop('checked')) {
            inputNormal.hide("slow");
            inputDateRange.show("slow");
        }
    }

    function displayMeetingOptions() {
        const meetingElement = $('#byMeeting');
        if(meetingElement.prop('checked'))
            importanceOption.show("slow");
        else
            importanceOption.hide("slow");
    }

    function init() {
        inputDateRange.css('display','block');
        inputDateRange.hide("fast");
        setTimeout(()=>{
            inputDateRange.css('visibility','visible');
        },1000);
        importanceOption.hide();
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