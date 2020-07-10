//para el formulario de cambiar contraseña en modifucaciones
function submitChangePasswordUser(){
    var params = {};
    params["id"] = $("#id").val();
    params["newPassword"] = $("#newPassword").val();
    params["confirmaPassword"] = $("#confirmaPassword").val();
    
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/cambiar-mi-clave",
        data: JSON.stringify(params),
        dataType: 'text',
        cache: false,
        timeout: 600000,
        /*aca entra si todo esta ok*/
        success: function (data) {
            $("#changePasswordForm")[0].reset();/*resetear el fromulario para que los campos se pongan en blanco*/
            
            $("#changePasswordError").hide();
            $("#formSuccess").show();
            $("#formSuccess").html("Clave actulizada correctamente, debe iniciar sesion para validar sus cambios");

            $('#PasswordModal').modal('toggle');/*togle es para que si esta visible lo oculte y si esta oculto lo muestra*/
            setTimeout(function(){	$("#formSuccess").hide('slow'); }, 5000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            
          },
        error: function (e) {
            
            $("#formSuccess").hide();
            $("#changePasswordError").show();
            $("#changePasswordError").html(e.responseText);/*aca le envia como texto lo que tenemos en el controller*/
            setTimeout(function(){	$("#changePasswordError").hide('slow'); }, 3000);/*hide slow es para que se esconda despacio y luego de  -2000 es dos seg*/
            
          }
    });

}