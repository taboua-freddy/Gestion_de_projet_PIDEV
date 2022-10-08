(function($) {
    'use strict';
    var EditableTable = {
        options: {
            addButton: '#addToTable',
            table: '#datatable-editable',
            dialog: {
                wrapper: '#dialog',
                cancelButton: '#dialogCancel',
                confirmButton: '#dialogConfirm',
            }
        },
        initialize: function() {
            this.setVars().build().events();
        },
        setVars: function() {
            this.$table = $(this.options.table);
            this.$addButton = $(this.options.addButton);
            this.dialog = {};
            this.dialog.$wrapper = $(this.options.dialog.wrapper);
            this.dialog.$cancel = $(this.options.dialog.cancelButton);
            this.dialog.$confirm = $(this.options.dialog.confirmButton);
            return this;
        },
        build: function() {
            this.datatable = this.$table.DataTable({
                paging: false,
                searching: false,
                ordering:  false,
                info: false,
                aoColumns: [null, {
                    "bSortable": false
                }],
                "language": {
                    "emptyTable": "Aucun objectif ajouté"
                },
            });
            window.dt = this.datatable;
            return this;
        },
        events: function() {
            var _self = this;
            this.$table.on('click', 'a.save-row', function(e) {
                e.preventDefault();
                _self.rowSave($(this).closest('tr'));
            }).on('click', 'a.cancel-row', function(e) {
                e.preventDefault();
                _self.rowCancel($(this).closest('tr'));
            }).on('click', 'a.edit-row', function(e) {
                e.preventDefault();
                _self.rowEdit($(this).closest('tr'));
            }).on('click', 'a.remove-row', function(e) {
                e.preventDefault();
                var $row = $(this).closest('tr');
                _self.rowRemove($row);
                /*$.magnificPopup.open({
                    items: {
                        src: '#dialog',
                        type: 'inline'
                    },
                    preloader: false,
                    modal: true,
                    callbacks: {
                        change: function() {
                            _self.dialog.$confirm.on('click', function(e) {
                                e.preventDefault();
                                _self.rowRemove($row);
                                $.magnificPopup.close();
                            });
                        },
                        close: function() {
                            _self.dialog.$confirm.off('click');
                        }
                    }
                });*/
            });
            this.$addButton.on('click', function(e) {
                e.preventDefault();
                _self.rowAdd();
            });
            this.dialog.$cancel.on('click', function(e) {
                e.preventDefault();
                $.magnificPopup.close();
            });
            return this;
        },
        rowAdd: function() {
            this.$addButton.attr({
                'disabled': 'disabled'
            });
            var actions, data, $row;
            actions = ['<input type="text" name="objectifs[]" class="hidden objectif" value=""> <a href="#" class="hidden on-editing save-row"><i class="fa fa-save"></i></a>', '<a href="#" class="hidden on-editing cancel-row"><i class="fa fa-times"></i></a>', '<a href="#" class="on-default edit-row"><i class="fa fa-pencil"></i></a>', '<a href="#" class="on-default remove-row"><i class="fa fa-trash-o"></i></a>'].join(' ');
            data = this.datatable.row.add(['', actions]);
            $row = this.datatable.row(data[0]).nodes().to$();
            $row.addClass('adding').find('td:last').addClass('actions');
            this.rowEdit($row);
            this.datatable.order([0, 'asc']).draw();
        },
        rowAddDefault: function(value) {
            this.$addButton.attr({
                'disabled': 'disabled'
            });
            var actions, data, $row;
            actions = ['<input type="text" name="objectifs[]" class="hidden objectif" value=""> <a href="#" class="hidden on-editing save-row"><i class="fa fa-save"></i></a>', '<a href="#" class="hidden on-editing cancel-row"><i class="fa fa-times"></i></a>', '<a href="#" class="on-default edit-row"><i class="fa fa-pencil"></i></a>', '<a href="#" class="on-default remove-row"><i class="fa fa-trash-o"></i></a>'].join(' ');
            data = this.datatable.row.add([value, actions]);
            $row = this.datatable.row(data[0]).nodes().to$();
            $row.addClass('adding').find('td:last').addClass('actions');
            this.rowEdit($row);
            this.rowSave($row);
            this.datatable.order([0, 'asc']).draw();
        },
        rowCancel: function($row) {
            var _self = this,
                $actions, i, data;
            if ($row.hasClass('adding')) {
                this.rowRemove($row);
            } else {
                data = this.datatable.row($row.get(0)).data();
                this.datatable.row($row.get(0)).data(data);
                $actions = $row.find('td.actions');
                if ($actions.get(0)) {
                    this.rowSetActionsDefault($row);
                }
                this.datatable.draw();
            }
        },
        rowEdit: function($row) {
            var _self = this,
                data;
            data = this.datatable.row($row.get(0)).data();
            $row.children('td').each(function(i) {
                var $this = $(this);
                if ($this.hasClass('actions')) {
                    _self.rowSetActionsEditing($row);
                } else {
                    $this.html('<input type="text" class="form-control input-block" value="' + data[i] + '"/>');
                }
            });
        },
        rowSave: function($row) {
            var _self = this,
                $actions, values = [];
            if ($row.hasClass('adding')) {
                this.$addButton.removeAttr('disabled');
                $row.removeClass('adding');
            }
            values = $row.find('td').map(function() {
                var $this = $(this);
                if ($this.hasClass('actions')) {
                    _self.rowSetActionsDefault($row);
                    return _self.datatable.cell(this).data();
                } else {
                    return $.trim($this.find('input').val());
                }
            });

            this.datatable.row($row.get(0)).data(values);
            $actions = $row.find('td.actions');
            if ($actions.get(0)) {
                this.rowSetActionsDefault($row);
            }
            this.datatable.draw();
        },
        rowRemove: function($row) {
            if ($row.hasClass('adding')) {
                this.$addButton.removeAttr('disabled');
            }
            this.datatable.row($row.get(0)).remove().draw();
        },
        rowSetActionsEditing: function($row) {
            $row.find('.on-editing').removeClass('hidden');
            $row.find('.on-default').addClass('hidden');
        },
        rowSetActionsDefault: function($row) {
            $row.find('.on-editing').addClass('hidden');
            $row.find('.on-default').removeClass('hidden');
            $row.find('.objectif').val($row.find('td:first').text());
        }
    };
    $(function() {
        var id_reunion = getIdReunion();
        EditableTable.initialize();

        $.ajax({
            type: "GET",
            url: "/activities/get_objectifs/"+id_reunion,
            dataType: "JSON",
            success: function (response) {
                for (let i = 0; i < response.length; i++) {
                    EditableTable.rowAddDefault(response[i]);
                }
            }
        });
    });
    function getIdReunion()
    {
        var form = $("#programmerForm");
        var idReunion = 0;
        if(form[0].hasAttribute('data-val'))
            idReunion = form.attr('data-val');

        return idReunion
    }
}).apply(this, [jQuery]);
