<?php

class ControllerAccueil
{
    public function __construct($url=null) // 2 _
    {
        require_once "view/viewAccueil.php";
    }
}

//j'oublie volontairement la balise de fermeture php