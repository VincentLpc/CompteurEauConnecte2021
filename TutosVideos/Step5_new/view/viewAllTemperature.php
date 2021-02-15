<?php

ob_start();
//affichage de toutes les températures

?>

<div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>Id</th>
              <th>idCapteur</th>
              <th>Instant d'acquisition</th>
              <th>Valeur</th>
            </tr>
          </thead>
          <tbody><?php
                foreach($data as $laData): ?>
            <tr>
                <td><?= $laData->getID() ?></td>
                <td><?= $laData->getIdCapteur() ?></td>
                <td><?= $laData->getDateTime() ?></td>
                <td><?= $laData->getValue() ?></td>
                <?php endforeach;?>
            </tr>
          </tbody>
        </table>
</div>





<?php
$content=ob_get_clean();
require ('template.php');