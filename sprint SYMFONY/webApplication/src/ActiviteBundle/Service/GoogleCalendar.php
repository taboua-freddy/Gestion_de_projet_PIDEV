<?php


namespace ActiviteBundle\Service;


use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Entity\Reunion;
use AppBundle\Entity\User;
use Exception;
use Google_Client;
use Google_Service_Calendar;
use Google_Service_Calendar_Event;

class GoogleCalendar
{
    private $authUrl = "";
    /** @var Google_Client */
    private $client;

    public function __construct()
    {
        $client = new Google_Client();
        $client->setApplicationName('Google Calendar API PHP Quickstart');
        $client->setScopes(Google_Service_Calendar::CALENDAR);
        $client->setAuthConfig(__DIR__ . "/credentials.json");
        $client->setAccessType('offline');
        $client->setPrompt('select_account consent');
        $this->client = $client;
        $this->authUrl = $this->client->createAuthUrl();
    }

    public function insertGoogleToken($authcode ,$email)
    {
        $client = $this->client;
        $tokenPath = __DIR__.'/token/'.$email.'token.json';
        if (file_exists($tokenPath)) {
            $accessToken = json_decode(file_get_contents($tokenPath), true);
            $client->setAccessToken($accessToken);
        }

        if ($client->isAccessTokenExpired()) {
            if ($client->getRefreshToken()) {
                $client->fetchAccessTokenWithRefreshToken($client->getRefreshToken());
            } else {
                $accessToken = $client->fetchAccessTokenWithAuthCode($authcode);
                $client->setAccessToken($accessToken);
               if (array_key_exists('error', $accessToken)) {
                    throw new Exception(join(', ', $accessToken));
                }
            }
            if (!file_exists(dirname($tokenPath))) {
                mkdir(dirname($tokenPath), 0700, true);
            }
            file_put_contents($tokenPath, json_encode($client->getAccessToken()));
        }
        $this->client = $client;
    }

    public function saveMeeting(array $event)
    {
        $service = new Google_Service_Calendar($this->client);

        $event = new Google_Service_Calendar_Event($event);

        $calendarId = 'primary';
        $event = $service->events->insert($calendarId, $event);
        //printf('Event created: %s\n', $event->htmlLink);

    }

    /**
     * @param Reunion $reunion
     * @return array
     * @throws Exception
     */
    public function setMeeting(Reunion $reunion)
    {
        $tabParticipants = [];
        foreach ($reunion->getPresenceReunion() as $presenceReunion)
        {
            /** @var User $participant */
            $participant = $presenceReunion->getuser();
            array_push($tabParticipants,["email"=>$participant->getEmail()]);
        }
        $event = [
            'summary' => $reunion->getTitre(),
            'description' => $reunion->getDescription(),
            'start' => [
                'dateTime' => $reunion->getDatedebut()->format("yy-m-d\TH:m:s"),
                'timeZone' => date_default_timezone_get(),
            ],
            'end' => [
                'dateTime' => $reunion->getDatefin()->format("yy-m-d\TH:m:s"),
                'timeZone' => date_default_timezone_get(),
            ],
            'recurrence' => [
                'RRULE:FREQ=DAILY;COUNT=1'
            ],
        ];
        if(!empty($tabParticipants))
            $event['attendees'] = $tabParticipants;
        if($reunion->getDaterappel()!=null && $reunion->getDatedebut()>$reunion->getDaterappel())
        {
            $rappel = $reunion->getDatedebut()->diff($reunion->getDaterappel())->i;

            $event['reminders'] = [
                'useDefault' => FALSE,
                'overrides' => [
                    ['method' => 'email', 'minutes' => $rappel],
                    ['method' => 'popup', 'minutes' => $rappel],
                ],
            ];
        }
        return $event;
    }

    public function setEvent(Evenement $event)
    {

        $eventt = [
            'summary' => $event->getTitre(),
            'description' => $event->getDescription(),
            'start' => [
                'dateTime' => $event->getDatedebut()->format("yy-m-d\TH:m:s"),
                'timeZone' => date_default_timezone_get(),
            ],
            'end' => [
                'dateTime' => $event->getDatefin()->format("yy-m-d\TH:m:s"),
                'timeZone' => date_default_timezone_get(),
            ],
            'recurrence' => [
                'RRULE:FREQ=DAILY;COUNT=1'
            ],
        ];

        if($event->getDaterappel()!=null && $event->getDatedebut()>$event->getDaterappel())
        {
            $rappel = $event->getDatedebut()->diff($event->getDaterappel())->i;

            $eventt['reminders'] = [
                'useDefault' => FALSE,
                'overrides' => [
                    ['method' => 'email', 'minutes' => $rappel],
                    ['method' => 'popup', 'minutes' => $rappel],
                ],
            ];
        }
        return $eventt;
    }

    /**
     * @return string
     */
    public function getAuthUrl()
    {
        return $this->authUrl;
    }


}