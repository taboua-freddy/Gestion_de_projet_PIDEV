<?php

namespace ActiviteBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Objectif
 *
 * @ORM\Table(name="objectif", indexes={@ORM\Index(name="reunion1", columns={"idReunion"})})
 * @ORM\Entity
 */
class Objectif implements \JsonSerializable
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idObjectif", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idobjectif;

    /**
     * @var string
     *
     * @ORM\Column(name="objectif", type="string", length=20, nullable=false)
     */
    private $objectif;

    /**
     * @var Reunion
     *
     * @ORM\ManyToOne(targetEntity="Reunion")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idReunion", referencedColumnName="idReunion" ,onDelete="CASCADE")
     * })
     */
    private $reunion;

    /**
     * Objectif constructor.
     * @param string $objectif
     * @param Reunion $reunion
     */
    public function __construct($objectif, Reunion $reunion)
    {
        $this->objectif = $objectif;
        $this->reunion = $reunion;
    }


    /**
     * @return int
     */
    public function getIdobjectif()
    {
        return $this->idobjectif;
    }

    /**
     * @param int $idobjectif
     */
    public function setIdobjectif($idobjectif)
    {
        $this->idobjectif = $idobjectif;
    }

    /**
     * @return string
     */
    public function getObjectif()
    {
        return $this->objectif;
    }

    /**
     * @param string $objectif
     */
    public function setObjectif($objectif)
    {
        $this->objectif = $objectif;
    }

    /**
     * @return \Reunion
     */
    public function getReunion()
    {
        return $this->reunion;
    }

    /**
     * @param \Reunion $reunion
     */
    public function setReunion($reunion)
    {
        $this->reunion = $reunion;
    }

    /**
     * @inheritDoc
     */
    public function jsonSerialize()
    {
        return get_object_vars($this);
    }

}

