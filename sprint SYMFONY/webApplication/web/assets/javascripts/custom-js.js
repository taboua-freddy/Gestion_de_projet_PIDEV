$(document).ready(function () {
    var titreInput = $(".fieldInput");
    var titreSelect = $(".selectInput");
    titreInput.remove();


    $(".selectProjet").on("change",function () {
        var groupSprint = $("#sprint");
        var projet = $(this).val();
        groupSprint.find("select").prop("value","");
        groupSprint.find(".select2-chosen").html("sélectionner un Sprint");
        if(projet=="")
        {
            groupSprint.fadeOut();
        }
        else
        {
            // recuperation des sprints du projet
            var options = `<option value="">sélectionner un Sprint</option>`;
            $.ajax({
                type: "GET",
                url: "/activities/get_sprint/"+projet,
                dataType: "JSON",
                success: function (data) {
                    for (let i = 0; i < data.length; i++) {
                        const sprint = data[i];
                        options += `<optgroup label="${sprint.date_debut}">
                                        <option value="${sprint.id}">${sprint.nom}</option>
                                    </optgroup>
                                    `;
                    }
                    groupSprint.find("select").html(options);
                }
            });
            groupSprint.fadeIn();
        }
    });
    $("#checkboxTitre").on("click",function () {
        var divBlock = $(".blockTitre");
        if($(this).prop("checked"))
        {
            divBlock.html(titreInput);
            divBlock.find(".selectInput").remove();
        }
        else
        {
            divBlock.html(titreSelect);
            divBlock.find(".fieldInput").remove();
        }
    });
    $("#checkboxRappel").on("click",function () {
        var divBlock = $(".blockRappel");
        if($(this).prop("checked"))
        {
            $(".blockRappel").fadeIn();
        }
        else
        {
            $(".blockRappel").fadeOut();
        }
    });
    $("#programmer").on("click",function (event) {
        event.preventDefault();
        const id_reunion = getIdReunion();
        const form = $("#programmerForm");
        $.ajax({
            type: "POST",
            url: form.attr("action"),
            data: form.serialize(),
            dataType: "JSON",
            beforeSend : function () {
                $('#programmer').addClass('disabled');
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                if(data['errors'].length===0)
                    $("#programmerForm")[0].reset();
                if(id_reunion!==0)
                    //setTimeout(()=>window.location.assign("/activities/display_meeting"),2000);
                $('#programmer').removeClass("disabled");
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('#programmer').removeClass("disabled");
                $('.loader').addClass('hidden');
            }
        });
    });
    $("#programmerEvent").on("click",function (event) {
        event.preventDefault();
        const id_evnt = getIdEvent();
        const form = $("#EventForm");
        var formD = document.getElementById("EventForm");
        var formData = new FormData(formD);
        $.ajax({
            type: "POST",
            url: form.attr("action"),
            data: formData,
            mimeType: "multipart/form-data",
            contentType: false,
            cache: false,
            processData:false,
            dataType: "JSON",
            beforeSend : function () {
                $('#programmerEvent').addClass('disabled');
                $('.loader').removeClass('hidden');
            },
            success: function (data) {
                displayMessage(data);
                if(data['errors'].length===0)
                {
                    $("#EventForm")[0].reset();
                    $('#programmerEvent').removeClass("disabled");
                }

                if(id_evnt!==0)
                    //setTimeout(()=>window.location.assign("/activities/display_meeting"),2000);
                $('#programmerEvent').removeClass("disabled");
                $('.loader').addClass('hidden');
            },
            error: function() {
                var notice = new PNotify( {
                        title: 'Notification', text: "Echec de la connexion avec le serveur", type: 'error', addclass: 'stack-topright',delay : 5000
                    }
                );
                $('#programmerEvent').removeClass("disabled");
                $('.loader').addClass('hidden');
            }
        });
    });

    function getIdReunion()
    {
        const form = $("#programmerForm");
        let idReunion = 0;
        if(form[0].hasAttribute('data-val'))
            idReunion = form.attr('data-val');

        return idReunion;
    }
    function getIdEvent()
    {
        const form = $("#EventForm");
        let idEvent = 0;
        if(form[0].hasAttribute('data-val'))
            idEvent = form.attr('data-val');

        return idEvent
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