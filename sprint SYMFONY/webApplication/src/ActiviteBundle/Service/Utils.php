<?php


namespace ActiviteBundle\Service;


use AppBundle\Entity\User;
use Doctrine\DBAL\Types\IntegerType;
use mysql_xdevapi\Collection;
use ProjetBundle\Entity\GroupeTravail;
use Symfony\Component\Form\FormInterface;

class Utils
{
    public function getTypeImpotance()
    {
        return $typePresence = ["0"=>"Pas important","3"=>"Moin important","5"=>"Très important"];
    }

    /**
     * @return array
     */
    public function getUniteTemps()
    {
        return [
            "1"=>"Minutes",
            "60"=>"Heures",
            "1340"=>"Jours"
        ];
    }

    /**
     * @return array
     */
    public function getColorCalendar()
    {
        return [
            0=>"#FFC312",
            3=>"#009432",
            5=>"#EA2027"
        ];
    }

    /**
     * @return array
     */
    public function getColorSearch()
    {
        return [
            0=>"warning",
            3=>"success",
            5=>"danger"
        ];
    }

    /**
     * @param $presence
     * @return string
     */
    public function getPresenceString($presence)
    {
        return $presence==1 ? "Sera présent" : "Sera absent";
    }

    /**
     * @return array
     */
    public function getTypeSprint()
    {
        return ["Sprint Planning","Sprint StandUp","Sprint Review","Sprint Retrospective"];
    }

    /**
     * @param $userss
     * @return array
     */
    public function groupeUser($userss)
    {
        $result = [];
        $result["Sans Groupe"] = [];

        /** @var User $user */
        foreach ($userss as $user)
        {
            /** @var Collection $groupes */
            $groupes = $user->getGroupe();
            if($groupes->count()==0)
                array_push($result["Sans Groupe"],$user);
            else
            {
                $nomGroupe = "";
                /** @var GroupeTravail $groupe */
                foreach ($groupes as $groupe)
                {
                    $nomGroupe .= $groupe->getNom() . "-";
                }
                $result["{$nomGroupe}"] = [];
                array_push($result["{$nomGroupe}"],$user);
            }


        }

        return $result;
    }

    public function getFormErrorsTree(FormInterface $form): array
    {
        $errors = [];

        if (count($form->getErrors()) > 0) {
            foreach ($form->getErrors() as $error) {
                $errors[] = $error->getMessage();
            }
        } else {
            foreach ($form->all() as $child) {
                $childTree = $this->getFormErrorsTree($child);

                if (count($childTree) > 0) {
                    $errors[$child->getName()] = $childTree;
                }
            }
        }

        return $errors;
    }


    // Date Utilities
//----------------------------------------------------------------------------------------------


    /**
     * Parses a string into a DateTime object, optionally forced into the given timeZone.
     * @param $string
     * @param null $timeZone
     * @return \DateTime
     * @throws \Exception
     */
    public function parseDateTime($string, $timeZone=null) {
        $date = new \DateTime(
            $string,
            $timeZone ? $timeZone : new \DateTimeZone('UTC')
        // Used only when the string is ambiguous.
        // Ignored if string has a timeZone offset in it.
        );
        if ($timeZone) {
            // If our timeZone was ignored above, force it.
            $date->setTimezone($timeZone);
        }
        return $date;
    }


    /**
     * Takes the year/month/date values of the given DateTime and converts them to a new DateTime,but in UTC.
     * @param $datetime
     * @return \DateTime
     * @throws \Exception
     */
    public function stripTime($datetime) {
        return new \DateTime($datetime->format('Y-m-d'));
    }

    /**
     * get meetings for javascript APT Calendar
     * @param $start
     * @param $end
     * @param $timeZone
     * @param $tabReunions
     * @return array
     * @throws \Exception
     */
    public function getReunion($start, $end, $timeZone, $tabReunions)
    {
        // Parse the start/end parameters.
        // These are assumed to be ISO8601 strings with no time nor timeZone, like "2013-12-29".
        // Since no timeZone will be present, they will parsed as UTC.

        $range_start = $this->parseDateTime($start);
        $range_end = $this->parseDateTime($end);

        // Parse the timeZone parameter if it is present.
        $time_zone = null;
        if ($timeZone) {
            $time_zone = new \DateTimeZone($time_zone);
        }

        // Read and parse our events JSON file into an array of event data arrays.
        $input_arrays = $tabReunions;

        // Accumulate an output array of event data arrays.
        $output_arrays = array();
        foreach ($input_arrays as $array) {

            // Convert the input array into a useful Event object
            $event = new Event($array, $time_zone);

            // If the event is in-bounds, add it to the output
            if ($event->isWithinDayRange($range_start, $range_end)) {
                $output_arrays[] = $event->toArray();
            }
        }
        return $output_arrays;
    }

    public function isSimpleUser(array $roles)
    {
        return !(in_array("SCRUM_MASTER",$roles) || in_array("PRODUCT_OWNER",$roles));
    }
}