<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ユーザ管理</title>
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs/dt-1.10.12/datatables.min.css"/>
</head>
<body>

<div class="container">

    <hr>
    <div class="row">
        <a class="btn btn-primary" href="/admin/user/">ユーザ追加</a>
    </div>
    <hr>
    <div class="row">

        <table id="userList" class="table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>ID</th>
                <th>ユーザ名</th>
                <th>Eメール</th>
                <th>変更</th>
            </tr>
            </thead>
            <tbody>

        <#list users as user>
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td><a href="/admin/user/${user.id}/">変更</a></td>
            </tr>
        </#list>
            </tbody>
        </table>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/bs/dt-1.10.12/datatables.min.js"></script>

<script>
    $(document).ready(function() {
        $('#userList').DataTable({
            'bSort' : false,
            'bFilter' : false,
            // 'bInfo' : false,
            'language': {
                "url": "//cdn.datatables.net/plug-ins/1.10.12/i18n/Japanese.json"
            }
        });
    });

</script>
</body>
</html>