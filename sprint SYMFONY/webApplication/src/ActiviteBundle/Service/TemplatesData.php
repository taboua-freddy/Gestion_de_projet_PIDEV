<?php


namespace ActiviteBundle\Service;


use ActiviteBundle\Entity\Evenement;
use ActiviteBundle\Entity\Objectif;
use ActiviteBundle\Entity\Reunion;
use AppBundle\Entity\User;
use Symfony\Component\Asset\Packages;

class TemplatesData
{
    private $basePath;

    /**
     * @param Reunion[] $reunions
     * @return string
     */
    public function templateSearchMeeting(array $reunions)
    {
        $utils = new Utils();
        $data = "";
        $data .= <<<HTML
            <div class="tm-body">
                
HTML;
        $groupDate = "";
        foreach ($reunions as $reunion) {
            $color = $utils->getColorSearch()[$reunion->getImportance()];
            if($groupDate=="" || $groupDate->format("M/yy") != $reunion->getDatedebut()->format("M/yy"))
            {
                $data .= <<<HTML
                <div class="tm-title">
                    <h3 class="h5 text-uppercase">{$reunion->getDatedebut()->format("M/yy")}</h3>
                </div>
                <ol class="tm-items">
                
HTML;
            }
            $data .= <<<HTML
                
                    <li>
                        <div class="tm-info">
                            <div class="tm-icon" ><i class="fa fa-map-marker"></i></div>
                            <time class="tm-datetime" datetime="2013-11-14 17:25" >
                                <div class="tm-datetime-date">{$reunion->getDatedebut()->format("d/M/yy")}</div>
                                <div class="tm-datetime-time">{$reunion->getDatedebut()->format("h:m")}</div>
                            </time>
                        </div>
                        <div class="tm-box " data-appear-animation="fadeInRight" data-appear-animation-delay="400">
                            <p>
                                <span class="text-{$color}">{$reunion->getTitre()} | <strong>MEETING</strong></span>
                            </p>
                            <blockquote class="{$color}">
                                <p>{$reunion->getDescription()}</p>
                            </blockquote>
        
                            <div class="tm-meta">
                                <span>
                                    <i class="fa fa-user"></i> Coodonateur <a href="#">{$reunion->getCoordonateur()}</a>
                                </span>
                            </div>
                        </div>
                    </li>
                </ol>
           
HTML;
            if($groupDate=="" || $groupDate->format("M/yy") != $reunion->getDatedebut()->format("M/yy"))
            {
                $groupDate = $reunion->getDatedebut();
                $data .= <<<HTML
                </ol> 
HTML;
            }
        }
            return $data;
    }

    /**
     * @param Evenement[] $events
     * @return string
     */
    public function templateSearchEvent(array $events)
    {
        $data = "";

        $groupDate = "";
        foreach ($events as $event)
        {
            $color = "dark";
            if($groupDate=="" || $groupDate->format("M/yy") != $event->getDatedebut()->format("M/yy"))
            {
                $data .= <<<HTML
                <div class="tm-title">
                    <h3 class="h5 text-uppercase">{$event->getDatedebut()->format("M/yy")}</h3>
                </div>
                
HTML;
            }
            $data .= <<<HTML
                
                <ol class="tm-items">
                    <li>
                        <div class="tm-info">
                            <div class="tm-icon"><i class="fa fa-map-marker"></i></div>
                            <time class="tm-datetime" datetime="2013-11-14 17:25">
                                <div class="tm-datetime-date">{$event->getDatedebut()->format("d/M/yy")}</div>
                                <div class="tm-datetime-time">{$event->getDatedebut()->format("h:m")}</div>
                            </time>
                        </div>
                        <div class="tm-box " data-appear-animation="fadeInRight" data-appear-animation-delay="400">
                            <p>
                                <span class="text-{$color}">{$event->getTitre()} | <strong>EVENT</strong> </span>
                            </p>
                            <blockquote class="{$color}">
                                <p>{$event->getDescription()}</p>
                            </blockquote>
        
                            <div class="tm-meta">
                                <span>
                                    <i class="fa fa-user"></i> Lieu <a href="#">{$event->getLieu()}</a>
                                </span>
                            </div>
                        </div>
                    </li>
                </ol>
HTML;
            if($groupDate=="" || $groupDate->format("M/yy") != $event->getDatedebut()->format("M/yy"))
            {
                $groupDate = $event->getDatedebut();
                $data .= <<<HTML
                </ol> 
HTML;
            }
        }
        $data .= <<<HTML
            </div>
                
HTML;
    return $data;
    }

    /**
     * @param Reunion $reunion
     * @param Objectif[] $objectifs
     * @return string
     */
    public function templateDetailsMeeting(Reunion $reunion,array $objectifs)
    {
        $importance = (new Utils())->getTypeImpotance()[$reunion->getImportance()];
        $data =  <<<HTML
        <p class="h4 text-light">Détails Réunion
        </p>
        <hr />
        <div class="form-group">
            <label class="col-md-3 control-label" for="titre">Titre</label>
            <div class="col-md-9">
                <input class="form-control" id="titre" value="{$reunion->getTitre()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="coordonateur">Coordonateur</label>
            <div class="col-md-9">
                <input class="form-control" id="coordonateur" value="{$reunion->getCoordonateur()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="sprint">Sprint</label>
            <div class="col-md-9">
                <input class="form-control" id="sprint" value="{$reunion->getSprint()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="debut">Debut</label>
            <div class="col-md-9">
                <input class="form-control" id="debut" value="{$reunion->getDatedebut()->format("d-M-yy H:m:s")}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="fin">Fin</label>
            <div class="col-md-9">
                <input class="form-control" id="fin" value="{$reunion->getDatefin()->format("d-M-yy H:m:s")}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="important">Importance</label>
            <div class="col-md-9">
                <input class="form-control" id="important" value="{$importance}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="theme">Theme</label>
            <div class="col-md-9">
                <input class="form-control" id="theme" value="{$reunion->getThemedujour()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="description">Description</label>
            <div class="col-md-9">
                <input class="form-control" id="description" value="{$reunion->getDescription()}" readonly="readonly" type="text" >
            </div>
        </div>

        <hr />
        <p class="mt-5 h4 text-light">Objectifs</p>
        <div class="panel-body">
            <div class="table-responsive">
                <table class="table mb-none">
                    <tbody>
HTML;
        if($objectifs==null)
        {
            $data .=  <<<HTML
                        <tr>
                            <td>Pas d'objectif</td>
                        </tr>
HTML;
        }else
            {
                foreach ($objectifs as $objectif) {
                $data .=  <<<HTML
                        <tr>
                            <td>{$objectif->getObjectif()}</td>
                        </tr>
HTML;
            }
        }

        $data .=  <<<HTML
                    </tbody>
                </table>
            </div>
        </div>
HTML;
        $data .= <<<HTML
        
HTML;
     return $data;
    }

    /**
     * @param Reunion $reunion
     * @return string
     * @throws \Exception
     */
    public function templateParticipantsMeeting(Reunion $reunion)
    {
        $presenceReunions = $reunion->getPresenceReunion();
        if (empty($presenceReunions))
            return '<tr class="center text-bold"><td colspan="4"><h2>Aucun participant</h2></td></tr>';
        $data  = "";
        for ($i=1 ; $i<=count($presenceReunions);$i++){
            /** @var array $presenceReunions */
            $presenceReunion = $presenceReunions[($i-1)];
            /** @var User $user */
            $user = $presenceReunion->getUser();

            if ($reunion->getDatedebut() > new \DateTime())
                $presence = ($presenceReunion->getPresence()==1)?"Sera Present":"Sera Absent";
            else
                $presence = ($presenceReunion->getPresence()==1)?"Etait Present":"Etait Absent";

            $color = $presenceReunion->getPresence()==1?"blue":"red";
            $data .=  <<<HTML
                <tr>
                    <td>{$i}</td>
                    <td>{$user->getNom()}</td>
                    <td>{$user->getPrenom()}</td>
                    <td style="color: {$color}">{$presence}</td>
                </tr>
HTML;
        }
        return $data;
    }

    /**
     * @param Evenement $event
     * @return string
     */
    public function templateDetailsEvent(Evenement $event)
    {
        $url  = "/uploads/images/events/".$event->getImageName();
        $data =  <<<HTML
        <p class="h4 text-light">Détails Evenement
        </p>
        <hr />
        <div class="form-group">
            <label class="col-md-3 control-label" for="titre">Titre</label>
            <div class="col-md-9">
                <input class="form-control" id="titre" value="{$event->getTitre()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="debut">Debut</label>
            <div class="col-md-9">
                <input class="form-control" id="debut" value="{$event->getDatedebut()->format("d-M-yy H:m:s")}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="fin">Fin</label>
            <div class="col-md-9">
                <input class="form-control" id="fin" value="{$event->getDatefin()->format("d-M-yy H:m:s")}" readonly="readonly" type="text" >
            </div>
        </div>
HTML;
        if(!empty($event->getImageName()))
        {

        $data .=  <<<HTML
        <div class="form-group">
            <label class="col-md-4 control-label" for="theme">Affiche</label>
            <div class="thumbnail-gallery col-md-8">
                <a class="img-thumbnail lightbox" href="{$url}" data-plugin-options='{ "type":"image" }'>
                    <img class="img-responsive" width="215" src="{$url}">
                    <span class="zoom">
                        <i class="fa fa-search"></i>
                    </span>
                </a>
            </div>
        </div>
HTML;

        }
        $data .=  <<<HTML
        <div class="form-group">
            <label class="col-md-3 control-label" for="theme">Lieu</label>
            <div class="col-md-9">
                <input class="form-control" id="theme" value="{$event->getLieu()}" readonly="readonly" type="text" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-md-3 control-label" for="description">Description</label>
            <div class="col-md-9">
                <input class="form-control" id="description" value="{$event->getDescription()}" readonly="readonly" type="text" >
            </div>
        </div>

        <hr />
        <p class="mt-5 h4 text-light">Projets</p>
        <div class="panel-body">
            <div class="table-responsive">
                <table class="table mb-none">
                    <tbody> 
HTML;
        if($event->getProjets()==null)
        {
            $data .=  <<<HTML
                        <tr>
                            <td>Pas de projet</td>
                        </tr>
HTML;
        }else
        {
            foreach ($event->getProjets() as $projet) {
                $data .=  <<<HTML
                        <tr>
                            <td>{$projet->getNom()}</td>
                        </tr>
HTML;
            }
        }

        $data .=  <<<HTML
                    </tbody>
                </table>
            </div>
        </div>
HTML;
     return $data;
    }

    /**
     * @return Packages
     */
    public function getBasePath()
    {
        return $this->basePath;
    }

    /**
     * @param Packages $basePath
     */
    public function setBasePath(Packages $basePath)
    {
        $this->basePath = $basePath;
    }


}