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
}
