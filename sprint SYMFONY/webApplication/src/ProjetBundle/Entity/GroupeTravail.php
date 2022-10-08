<?php

namespace ProjetBundle\Entity;

use AppBundle\Entity\User;
use ProjetBundle\Entity\Projet;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
/**
 * GroupeTravail
 *
 * @ORM\Table(name="groupe_travail")
 * @ORM\Entity(repositoryClass="ProjetBundle\Repository\GroupeTravailRepository")
 */
class GroupeTravail
{
    /**
     * @var integer
     *
     * @ORM\Column(name="idGroupe", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idgroupe;


    /**
     * @var string
     *
     * @ORM\Column(name="nom", type="string", length=20, nullable=true)
     * @Assert\NotBlank
     * @Assert\Regex(
     *     pattern="/\d/",
     *     match=false,
     *     message="Your name cannot contain a number"
     * )
     */
    private $nom;

    /**
     * @var \Projet
     * @ORM\ManyToOne(targetEntity="Projet")
     * @ORM\JoinColumn(referencedColumnName="idProjet")
     */
    private $IdProjet;

    /**
     * @var Collection|User[]
     *
     * @ORM\ManyToMany(targetEntity="AppBundle\Entity\User"  ,mappedBy="groupe" , cascade={"persist","merge"})
     */
    private $users;
    public function __construct()
    {
        $this->users = new ArrayCollection();
    }
    public function addUser(User $user)
    {
        if ($this->users->contains($user)) {
            return;
        }

        $this->users->add($user);
        $user->addUserGroup($this);
    }

    /**
     * @param User $user
     */
    public function removeUser(User $user)
    {
        if (!$this->users->contains($user)) {
            return;
        }

        $this->users->removeElement($user);
        $user->removeUserGroup($this);
    }
    public function getusers(): Collection
    {
        return $this->users;
    }

    /**
     * @return int
     */
    public function getIdgroupe(): int
    {
        return $this->idgroupe;
    }

    /**
     * @return string
     */
    public function getNom()
    {
        return $this->nom;
    }

    /**
     * @param string $nom
     */
    public function setNom(string $nom)
    {
        $this->nom = $nom;
    }

    /**
     * @return Projet
     */
    public function getIdProjet()
    {
        return $this->IdProjet;
    }

    /**
     * @param Projet $IdProjet
     */
    public function setIdProjet($IdProjet)
    {
        $this->IdProjet = $IdProjet;
    }


}
