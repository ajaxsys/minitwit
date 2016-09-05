<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ユーザ管理 - ユーザ詳細</title>
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>
</head>
<body>

<div class="container" id="container">

    <hr>
    <div class="row">

        <form id="userDetail" method=""
        <#if user.id gt 0>
              action="/admin/user/${user.id!}/"
        <#else>
              action="/admin/user/"
        </#if>
            >

            <input class="hidden" type="hidden" value="${user.id!}" name="userId">
            <div class="form-group row">
                <label for="userName" class="col-xs-2 col-form-label">ユーザ名</label>
                <div class="col-xs-10">
                    <input class="form-control" type="text" value="${user.username!}" pattern=".{1,}" required title="１文字以上が必須" name="userName" id="userName">
                </div>
            </div>
            <div class="form-group row">
                <label for="email" class="col-xs-2 col-form-label">Eメール</label>
                <div class="col-xs-10">
                    <input class="form-control" type="search" value="${user.email!}" pattern=".{6,}" required title="６文字以上が必須" name="email" id="email">
                </div>
            </div>

<#if user.id gt 0>
            <button type="submit" class="btn btn-primary" method="put">更新</button>
            <button type="submit" class="btn btn-danger" method="delete">削除</button>
            <a class="btn btn-warning" href="/admin/users/">キャンセル</a>
<#else>
            <button type="submit" class="btn btn-primary" method="post">追加</button>
</#if>

        </form>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.3/jquery.min.js"></script>

<script>

    var $form =　$('#userDetail')

    $('button').click(function () {
        $form.attr('method', $(this).attr('method'))
    })

    $form.submit(function (e) {
        $.ajax({
            url: $form.attr('action'),
            type: $form.attr('method'),
            data: {
                userName : $('#userName').val(),
                email : $('#userName').val()
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            },
            success: function(responseData){
                console.log("Succes" + responseData);
                window.location.href="/admin/users/";
            }
        });

        e.preventDefault();
        return false;
    })

</script>
</body>
</html>