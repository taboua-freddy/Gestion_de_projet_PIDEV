$(document).ready(function () {
    var data=[];
    const idReunion = getIdReunion();
    $.ajax({
            type: "GET",
            url: "/activities/get_participants/"+idReunion,
            dataType: "JSON",
            success: function (response) {
                setListeParticipants(response['participants'],response['participantsSelect'])
        }
    });

    function getIdReunion()
    {
        const form = $("#programmerForm");
        let idReunion = 0;
        if(form[0].hasAttribute('data-val'))
            idReunion = form.attr('data-val');

        return idReunion
    }
    function setListeParticipants(data,selectedVal)
    {
        var autocomplete = new SelectPure(".autocomplete-select", {
            options: data,
            value: selectedVal,
            multiple: true,
            autocomplete: true,
            icon: "fa fa-times",
            placeholder:"ajouter des participants Ici..." ,
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