<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>BakMix levering ${recallmail.orderId} terugroepactie</title>
</head>
<body>
<div style="margin-top: 10px">Beste ${recallmail.customerName}, </div>
<br>
<div>Helaas is uw bestelling onderwerp van een terugroepactie!</div>
<br/>
<div>Gelieve onderstaande producten niet te consumeren:</div>
<ul>
   ${recallmail.affectedProducts?no_esc}
</ul>
<div>Onze excuses voor dit ongemak. U kan ons terugmailen voor een compensatie.</div>
<br>
<div>BakMix.be</div>
</body>
</html>
