<?php

ob_start();
//affichage de toutes les températures

?>

<!-----------------------------------------------------------
On convertit les variables PHP en javascript pour pouvoir les
utiliser dans le fichier .js qui affiche le graphe
------------------------------------------------------------>

<script type="text/javascript">
  var lesDates=<?= json_encode($lesDates) ?>;
  var lesMesures=<?= json_encode($lesMesures) ?>;
</script>



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
                <td>
                  <a href=<?=Router::makeURL("temperature/".$laData->getIdCapteur())?> >
                    <?=$laData->getIdCapteur() ?>
                  </a>
                <td><?= $laData->getDateTime() ?></td>
                <td><?= $laData->getValue() ?></td>
             </tr>
            <?php endforeach;?>
          </tbody>
        </table>
</div>


<canvas id="myChart" width="400" height="400"></canvas>

<?php
$content=ob_get_clean();
require ('template.php');