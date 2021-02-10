<?php
class TemperatureManager extends Model
{

    /*======================================================
    Constructeur
    ======================================================*/
    public function __construct()
    {
        $this->setBdd('db_capteur');
    }

    /*==========================================================
    Récupération de toutes les températures
    =========================================================*/

    public function getAllTemp()
    {
        //Ici, je simule les températures renvoyées par la base de données
        $var=[];

       //cherchons les températures dans la bas de données
        $var=$this->getAll("tb_capteur_temp","Temperature");

        return $var;
    }

    /*==========================================================
    Récupération des températures d'un capteur précis
    ===========================================================*/
    public function getTemp($idCapteur)
    {
        $var=[];
        $var=$this->getPartial("tb_capteur_temp","Temperature","idCapteur=".$idCapteur);
        return $var;
    }
}