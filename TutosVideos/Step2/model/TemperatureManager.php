<?php
class TemperatureManager
{
    public function getAllTemp()
    {
        //Ici, je simule les températures renvoyées par la base de données
        $var=[];

        $temp= array(array( "id"=>"1",
                            "idCapteur"=>451,
                            "dateTime"=>"2020-07-15 19:16:45",
                            "value"=>22.56),

                    array(  "id"=>"2",
                            "idCapteur"=>451,
                            "dateTime"=>"2020-07-15 19:16:50",
                            "value"=>22.32),
                            
                    array(  "id"=>"3",
                            "idCapteur"=>457,
                            "dateTime"=>"2020-07-15 19:16:56",
                            "value"=>28.32)
                    );
        foreach($temp as $laTemperature):
            $var[]=new Temperature($laTemperature);
        endforeach;

        return $var;
    }
}