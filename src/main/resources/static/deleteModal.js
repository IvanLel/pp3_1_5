$('document').ready(function () {
    $('.table .deleteButton').on('click', function (event){

        event.preventDefault();

        let href = $(this).attr('href')

        $.get(href, function (userForDelete, status) {

            $("#deleteId").val(userForDelete.id);
            $("#deleteUsername").val(userForDelete.username);
            $("#deletePassword").val(userForDelete.password);
            $("#deleteEmail").val(userForDelete.email);
            $("#deleteAge").val(userForDelete.age);

            $('input[type=checkbox]').prop('checked', false);

            userForDelete.roles.forEach(function(role) {
                $('input[type=checkbox][value=' + role.id + ']').prop('checked', true);
            });

        });
        $('#deleteUserModal').modal();

    });
});
