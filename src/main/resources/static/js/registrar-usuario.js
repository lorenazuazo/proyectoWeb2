//para saber si el usuarioe esta o no logueado
function check_usuario_registrado(){
    debugger;
    var usario_logueado = $('#username').val();
    if(usario_logueado == ""){
        alert("Debe registrarse o iniciar para realizar una reserva");
        $('#registrarUsuarioModal').modal('toggle');
        return false;
    }else {

        return true;
    }
}




//para el formulario de cambiar contraseña en modifucaciones
function registrarUsuario(){

    var params = {};
    params["nombre"] = $("#nombreDto").val();
    params["apellido"] = $("#apellidoDto").val();
    params["correo"] = $("#correoDto").val();
    params["dni"] = $("#dniDto").val();
    params["telefono"] = $("#telefonoDto").val();
    params["username"] = $("#usernameDto").val();
    params["password"] = $("#passwordDto").val();
    params["confirmPassword"] = $("#confirmPasswordDto").val();

    debugger;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/registrar-usuario",
        data: JSON.stringify(params),
        dataType: 'text',
        cache: false,//esto es para evitar que me traiga lo que tenga en cache y se conecte si osi con el servidor
        timeout: 600000,
        
        /*aca entra si todo esta ok*/
        success: function (data) {
            $("#newUserForm")[0].reset();/*resetear el fromulario para que los campos se pongan en blanco*/
            
            $("#registroError").hide();
            $("#formSuccess").show();
            $("#formSuccess").html("Se ha registrado con exito");
            //asigno el el fromulario para hacer la reserva los datos del usuario registrdo asi podra hacer la reserva
            document.getElementById('username').value = params["username"];
            document.getElementById('nombre').value = params["nombre"];
            document.getElementById('apellido').value = params["apellido"];

            $('#registrarUsuarioModal').modal('toggle');/*togle es para que si esta visible lo oculte y si esta oculto lo muestra*/
            setTimeout(function(){	$("#formSuccess").hide('slow'); }, 3000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            
          },
        error: function (e) {
            
            $("#formSuccess").hide();
            $("#registroError").show();
            $("#registroError").html(e.responseText);/*aca le envia como texto lo que tenemos en el controller*/
            setTimeout(function(){	$("#registroError").hide('slow'); }, 3000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            var mensaje=e.responseText;            
            if(mensaje == 'Usted ya tiene una cuenta Inicie Sesion'){
                $('#registrarUsuarioBtn').hide();
            }
          }
    });

}