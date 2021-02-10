<?php
ob_start();
//affichage de toutes les températures

foreach ($data as $laData): ?>
    <h2><?= $laData->getId() ?></h2>
    <p><?= $laData->getIdCapteur() ?></p>
    <time><?= $laData->getDateTime() ?></time>
    <time><?= $laData->getValue() ?></p>

<?php endforeach;?>

<?php
$content=ob_get_clean();
require('template.php');