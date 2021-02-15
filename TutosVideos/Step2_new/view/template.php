<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= $title ?></title>
</head>
<body>
    <header>
        <h2>Bienvenue sur mon site MVC</h2>
    </header>

    <!-- c'est ici que viens ce greffer le bloc généré par la vue associé -->
    <?= $content ?>

    <footer>
        <p>Créé par Lyne FRIEDERICH</p>
    </footer>

</body>
</html>