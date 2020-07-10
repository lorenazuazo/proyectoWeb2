//para ocultar los formularios
function Ocultar(id){
    document.getElementById(id).style.display = "none";
}
//para delete
function confirmDelete(id){
	$('#deleteModal').modal('show');
	$("#Input").val(id);
}agregahuesped

function deleteUser(){
	var id = $("#Input").val();
    window.location = "/modificaciones/deleteUser/"+id;
}

function deleteHabitacion(){
	var id = $("#Input").val();
    window.location = "/modificaciones/deleteHabitacion/"+id;
}

function deleteReserva(){
	var id = $("#Input").val();
    window.location = "/modificaciones/deleteReserva/"+id;
}

function deleteCaracteristica(){
	var id = $("#Input").val();
    window.location = "/modificaciones/eliminar-caracteristica/"+id;
}

function deleteTipohabitacion(){
	var id = $("#Input").val();
    window.location = "/modificaciones/eliminar-tipoHabitacion/"+id;
}

function deleteRoles(){
	var id = $("#Input").val();
    window.location = "/modificaciones/eliminar-Rol/"+id;
}

function deleteHuesped(id){
	var id = $("#Input").val();
    window.location = "/modificaciones/eliminar-Huesped/"+id;
}

//para el formulario de cambiar contraseña en modifucaciones
function submitChangePassword(){
    var params = {};
    params["id"] = $("#id").val();
    //params["passwordActual"] = $("#passwordActual").val();
    params["newPassword"] = $("#newPassword").val();
    params["confirmaPassword"] = $("#confirmaPassword").val();
    
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/modificaciones/cambiar-clave",
        data: JSON.stringify(params),
        dataType: 'text',
        cache: false,
        timeout: 600000,
        /*aca entra si todo esta ok*/
        success: function (data) {
            $("#changePasswordForm")[0].reset();/*resetear el fromulario para que los campos se pongan en blanco*/
            
            $("#changePasswordError").hide();
            $("#formSuccess").show();
            $("#formSuccess").html("Clave actulizada correctamente");

            $('#changePasswordModal').modal('toggle');/*togle es para que si esta visible lo oculte y si esta oculto lo muestra*/
            setTimeout(function(){	$("#formSuccess").hide('slow'); }, 3000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            
          },
        error: function (e) {
            
            $("#formSuccess").hide();
            $("#changePasswordError").show();
            $("#changePasswordError").html(e.responseText);/*aca le envia como texto lo que tenemos en el controller*/
            setTimeout(function(){	$("#changePasswordError").hide('slow'); }, 3000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            
          }
    });

}


//agrega los huesped nuevos a la tabla

/*function agregarHuesped(){
    var _nom = document.getElementById("nombreHuesped").value;
    var _ape = document.getElementById("apellidohuesped").value;
    var _dni = document.getElementById("dnihuesped").value;

    var fila="<tr><td>"+_nom+"</td><td>"+_ape+"</td><td>"+_dni+"</td><td><input class='form-check-input' type='checkbox' th:field='${reservaForm.huespedGrupo}' th:value='${huesped.id}' id='defaultCheck1' checked></td></tr>";

    var btn = document.createElement("TR");
   	btn.innerHTML=fila;
    document.getElementById("nuevohuesped").appendChild(btn);
}*/

function agregahuesped(){
    $('#agregahuesped').modal('show');
    var id = document.getElementById('idReserva').value;
    document.getElementById('reservahuesped').value=id;
    
}


//configuraciones de dataTable
$(document).ready(function () {
    $('#ListDataTable').DataTable({
        "language": {
            "decimal": "",
            "emptyTable": "No hay datos disponibles",
            "info": "_TOTAL_ registros encontrados",
            "infoEmpty": "No hay datos disponibles.",
            "infoFiltered": "(_MAX_ registros totales)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "_MENU_ registros",
            "loadingRecords": "Buscando...",
            "processing": "Procesando...",
            "search": "Buscar:",
            "zeroRecords": "Registro no encontrado",
            "paginate": {
                "first": "Primero",
                "last": "Ultimmo",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "aria": {
                "sortAscending": ": activate to sort column ascending",
                "sortDescending": ": activate to sort column descending"
            }
        }
    });
});