<?php
class ControllerTemperature
{
    public function __construct($url)
    {
        if ($url=='')
        throw new Exception('Page introuvable');

        //si juste 1 argument, on récupère toutes les températures
        else if (isset($url)&& count($url)==1)
        {
            $this->temperatureManager=new TemperatureManager;
            $data=$this->temperatureManager->getAllTemp();
            $title="Températures mesurées";
            require_once 'view/viewAllTemperature.php';

        }
    }
    
}