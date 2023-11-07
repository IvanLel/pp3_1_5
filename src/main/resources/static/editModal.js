$('document').ready(function () {
    $('.table .editingButton').on('click', function (event) {
        event.preventDefault();

        let href = $(this).attr('href')

        $.get(href, function (userForEdit, status) {
            $("#editId").val(userForEdit.id);
            $("#editUsername").val(userForEdit.username);

            $("#oldPassword").val(userForEdit.password);
            $("#editPassword").val('');

            $("#editEmail").val(userForEdit.email);
            $("#editAge").val(userForEdit.age);

            $('input[type=checkbox]').prop('checked', false);

            userForEdit.roles.forEach(function (role) {
                $('input[type=checkbox][value=' + role.id + ']').prop('checked', true);
            });
        });

        $('#editUserModal').modal();
    });

    if ($("#hasErrors").val() === "true") {
        $('#editUserModal').modal('show');
    }
});
