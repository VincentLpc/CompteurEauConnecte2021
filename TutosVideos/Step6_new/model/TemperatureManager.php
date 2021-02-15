<?php

class TemperatureManager extends Model
{

    /*=====================================================
    constructeur
    =====================================================*/
    public function __construct()
    {
        $this->setBdd('db_capteur');
    }

    /*====================================================
    Récupération de toutes les températures
    ====================================================*/

    public function getAllTemp()
    {
        //ici, je simule les températures renvoyées par la base de données

        $var=[];

        //cherchons les températures dans la base de données

        $var=$this->getAll("tb_capteur_temp","Temperature");

        return $var;

    }

    /*======================================================
    Récupération des températures d'un capteur précis
    =======================================================*/

    public function getTemp($idCapteur)
    {
        $var=[];
        $var=$this->getPartial("tb_capteur_temp","Temperature","idCapteur=".$idCapteur);
        return $var;
    }

    /*======================================================
    Récupération de toutes les valeurs mesurées sur un capteur précis
    ======================================================*/
    public function getValues($idCapteur)
    {
        $var=[];
        $sql="SELECT value from tb_capteur_temp WHERE idCapteur=".$idCapteur." ORDER BY dateTime";
        //var_dump($sql);die();
        $req=$this->getBdd()->prepare($sql);
        $req->execute();
        while($data=$req->fetch(PDO::FETCH_NUM))
        {
            $var[]=$data[0];
        }
        //var_dump($var);die();
        return $var;
    }
    /*================================================================
    Récupération de toutes les dates des températures mesurées sur un capteur précis
    ================================================================*/

    public function getDate($idCapteur)
    {
        $var=[];
        $formatDate_us="Y-m-d H:i:s";
        $formatDate_fr="d/m/Y H:i:s";

        $sql="SELECT dateTime from tb_capteur_temp WHERE idCapteur=".$idCapteur." ORDER BY dateTime";

        $req=$this->getBdd()->prepare($sql);
        $req->execute();
        while($data=$req->fetch(PDO::FETCH_NUM))
        {
            $date=DateTime::createFromFormat($formatDate_us,$data[0]);

            $var[]=$date->format($formatDate_fr);
        }
        //var_dump($var);die();
        return $var;
    }
}
