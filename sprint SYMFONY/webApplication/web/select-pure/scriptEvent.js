$(document).ready(function () {
    var data=[];
    const idEvent = getIdEvent();
    $.ajax({
        type: "GET",
        url: "/activities/get_projets/"+idEvent,
        dataType: "JSON",
        success: function (response) {
            console.log(response);
            setListeParticipants(response['projets'],response['projetsSelect'])
        }
    });

    function getIdEvent()
    {
        const form = $("#EventForm");
        let idEvent = 0;
        if(form[0].hasAttribute('data-val'))
            idEvent = form.attr('data-val');

        return idEvent
    }
    function setListeParticipants(data,selectedVal)
    {
        var autocomplete = new SelectPure(".autocomplete-select", {
            options: data,
            value: selectedVal,
            multiple: true,
            autocomplete: true,
            icon: "fa fa-times",
            placeholder:"ajouter des projets Ici..." ,
            onChange: value => { console.log(value); },
            classNames: {
                select: "select-pure__select",
                dropdownShown: "select-pure__select--opened",
                multiselect: "select-pure__select--multiple",
                label: "select-pure__label",
                placeholder: "select-pure__placeholder",
                dropdown: "select-pure__options",
                option: "select-pure__option",
                autocompleteInput: "select-pure__autocomplete",
                selectedLabel: "select-pure__selected-label",
                selectedOption: "select-pure__option--selected",
                placeholderHidden: "select-pure__placeholder--hidden",
                optionHidden: "select-pure__option--hidden",
            }
        });
        $("#clear").on("click",function (event) {
            event.preventDefault();
            autocomplete.reset();

        });
    }


});