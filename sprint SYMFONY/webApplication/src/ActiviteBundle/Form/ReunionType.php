<?php

namespace ActiviteBundle\Form;

use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Tests\Extension\Core\Type\DateTimeTypeTest;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ReunionType extends AbstractType
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
        $builder->setAction($options["action"])
            ->add('titre')
            ->add('datedebut',DateTimeType::class,$settingsDate)
            ->add('datefin',DateTimeType::class,$settingsDate)
            ->add('description')
            ->add('themedujour')
            ->add('importance',TextType::class)
            ->add('daterappel',DateTimeType::class,$settingsDate)
            ->add('sprint')
            ->add('coordonateur')
            //->add('presenceReunion')
        ;
    }

    /**
     * {@inheritdoc}
     */
    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults(array(
            'data_class' => 'ActiviteBundle\Entity\Reunion'
        ));
    }

    /**
     * {@inheritdoc}
     */
    public function getBlockPrefix()
    {
        return 'activitebundle_reunion';
    }


}
