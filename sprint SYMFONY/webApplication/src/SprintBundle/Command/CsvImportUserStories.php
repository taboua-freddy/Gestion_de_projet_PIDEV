<?php
/**
 * Created by PhpStorm.
 * User: arafe
 * Date: 09/04/2020
 * Time: 11:58
 */

namespace SprintBundle\Command;


use League\Csv\Reader;
use Doctrine\ORM\EntityManagerInterface;
use SprintBundle\Entity\UserStory;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Question\Question;
use Symfony\Component\Console\Style\SymfonyStyle;

class CsvImportUserStories extends Command
{

    /**
     * @var EntityManagerInterface
     */
    private $em;


    public function __construct(EntityManagerInterface $em)
    {
        parent::__construct();
        $this->em = $em;
    }

    protected function configure()
    {
        $this->setName('csv:import:userstories')
            ->setDescription('Imports a mock CSV file');

        $this
            ->addArgument(
                'fileRoute',
                InputArgument::OPTIONAL,
                'Please specify the route to file that will be imported'
            );
    }

    protected function execute(InputInterface $input, OutputInterface $output)
    {

        $io = new SymfonyStyle($input, $output);

        if ($input->getArgument('fileRoute') == null) {
            $helper = $this->getHelper('question');
            $ask = new Question('Provide the route to file : ');
            $routeFile = $helper->ask($input, $output, $ask);
        } else {
            $routeFile = $input->getArgument('fileRoute');
        }

        $reader = Reader::createFromPath($routeFile, 'r');
        $results = $reader->getIterator();

        $io->title('Attempting to import the feed .. ');
        $io->progressStart(iterator_count($results));

        foreach ($results as $k => $row) {
            if ($k < 1) continue;
            $p = $this->em->getRepository('ProjetBundle:Projet')->find($row[0]);

            $us = new UserStory();
            $us->setProjet($p);
            $us->setNomstory($row[1]);
            $us->setBv($row[2]);
            $us->setC($row[3]);
            $us->setPriorite($row[4]);
            $us->setComplexite($row[5]);
            $us->setDescription($row[6]);
            $us->setRoi($row[7]);
            $us->setStatus($row[8]);

            $this->em->persist($us);

            $this->em->flush();
        }


        $io->progressFinish();
        $io->success('Everything went well ');
    }

}