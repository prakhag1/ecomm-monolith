<!DOCTYPE html>
<html>
    <head>
        <title>Home page</title>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
    	<title>Online Boutique</title>
   		<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
    </head>
    <body>
        <header>
        <div class="navbar navbar-dark bg-dark box-shadow">
            <div class="container d-flex justify-content-between">
                <a href="/" class="navbar-brand d-flex align-items-center">
                    Online Boutique
                </a>
                <form class="form-inline ml-auto" id="currency_form">
                    <select name="currency_code" class="form-control" style="width:auto;">
                        <option value="USD">USD</option>
                    </select>
                    <a class="btn btn-primary btn-light ml-2" href="/showcart" role="button">View Cart (${cart_size})</a>
                </form>
            </div>
        </div>
    </header>
