<?php

$title='Vue Accueil';

ob_start(); //on place en mémoire tampon tout le code html qui suit

?>

<h1>Ma superbe page d'accueil</h1>
<p>Bienvenue sur notre site MVC</p>

<?php
$content = ob_get_clean();

require('template.php');