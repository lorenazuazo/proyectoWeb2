//para el calendario de la reserva-no deja seleccionar fechas ya pasadas
//y en checkOut solo fecha a partir de la seleccionada
//para que fecha checkOut no sea menos a checkIn
var today = new Date();

//fecha para checkIn
var anioIn = today.getFullYear();
var diaIn = today.getDate();
var _mesIn = today.getMonth(); //viene con valores de 0 al 11
_mesIn = _mesIn + 1; //con esto hago los meses de 1 al 12
//se le agrega un 0 para el formato date
if (_mesIn < 10) {
    var mesIn = "0" + _mesIn;
} else {
    var mesIn = _mesIn.toString();
}

if (diaIn < 10) {
    var _diaIn = "0" + diaIn;
} else {
    var _diaIn = diaIn.toString();
}
var fecha_minIn = anioIn + '-' + mesIn + '-' + _diaIn;  

//para los placeholder de fechas
document.getElementById('fechaReservaIn').value = fecha_minIn;
document.getElementById('fechaReservaOut').value = fecha_minIn;

//establece los minimos aceptables en fechas en placeholder
$('#fechaReservaIn').on('click', function() {
    document.getElementById('fechaReservaIn').value = fecha_minIn;
    document.getElementById('fechaReservaOut').value = fecha_minIn;
});

//establece los minimos aceptables en checkin
document.getElementById("fechaReservaIn").setAttribute("min",fecha_minIn);

//establece los minimos aceptables en checkout
$('#fechaReservaOut').on('click', function() {
    var date = $('#fechaReservaIn').val();
     if(date==""){
        document.getElementById("fechaReservaOut").setAttribute("min",fecha_minIn);
     }else{  
        document.getElementById("fechaReservaOut").setAttribute("min",date);
     }
});
//controla que la fecha de salida no sea menor a fecha de entrada
function validar_fechas(){
    var fechaReservaOut = $('#fechaReservaOut').val();
    var fechaReservaIn = $('#fechaReservaIn').val();
	if(fechaReservaOut < fechaReservaIn ){
        alert("La fecha de salida debe ser mayor a la fecha de ingreso");
        return false;
    }else{
        return true;
    }  
}

