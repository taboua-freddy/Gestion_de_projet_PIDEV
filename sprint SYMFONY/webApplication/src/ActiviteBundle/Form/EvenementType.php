<?php

namespace ActiviteBundle\Form;

use Doctrine\ORM\EntityRepository;
use ProjetBundle\Entity\Projet;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Vich\UploaderBundle\Form\Type\VichImageType;

class EvenementType extends AbstractType
{
    /**
     * {@inheritdoc}
     */
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $settingsDate = [
            'date_format'=>'dd/MM/yyyy',
            'date_widget'=>'single_text',
            'time_widget'=>'single_text'
        ];
        $builder->setAction($options['action'])
            ->add('titre')
            ->add('datedebut',DateTimeType::class,$settingsDate)
            ->add('datefin',DateTimeType::class,$settingsDate)
            ->add('description')
            ->add('lieu')
            ->add('affiche')
            ->add('imageFile',VichImageType::class)
            ->add('daterappel',DateTimeType::class,$settingsDate)
            /*->add('projets', EntityType::class, [
                'class' => Projet::class,
                'query_builder' => function (EntityRepository $er) {
                    return $er->createQueryBuilder('p')
                        ->orderBy('p.nom', 'ASC');
                },
                'choice_label' => 'nom',
            ])*/;


        /*'*/
    }/**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'ActiviteBundle\Entity\Evenement'
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function getBlockPrefix()
    {
        return 'activitebundle_evenement';
    }


}
