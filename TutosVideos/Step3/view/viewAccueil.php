<?php
$title='Vue Accueil';

ob_start(); //On place en mémoire tampon tout le code html qui suit
?>
<h1>Ma superbe page d'accueil</h1>
<p>Bonjour à tous</p>

<?php
$content = ob_get_clean();

require('template.php');