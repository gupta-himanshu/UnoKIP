$(function () {
        $('#datetimepicker6').datetimepicker({format: 'DD/MM/YYYY HH:mm:ss'});
        $('#datetimepicker7').datetimepicker({format: 'DD/MM/YYYY HH:mm:ss'});
        $("#datetimepicker6").on("dp.change", function (e) {
            $('#datetimepicker7').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker7").on("dp.change", function (e) {
            $('#datetimepicker6').data("DateTimePicker").maxDate(e.date);
        });
    });