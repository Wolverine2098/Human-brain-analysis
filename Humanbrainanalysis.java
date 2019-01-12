import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class mlwhole {

		
		 public static void main(String[] args) throws Exception {
			    
			    // load the CSV file
			    CSVLoader load = new CSVLoader();
			    load.setSource(new File("C:\\Users\\Logan\\Desktop\\ML\\linearregression\\headbrain.csv"));
			   Instances data = load.getDataSet();//get instances object
	            
			   ArffSaver save = new ArffSaver();
			  save.setInstances(data);//set the dataset we want to convert
			    
			    save.setFile(new File("C:\\Users\\Logan\\Desktop\\ML\\headbrain.arff"));
			    System.out.println("The .arff file format is as follows");
			    save.writeBatch();
			    System.out.println(data);
			     
			  //load the dataset
				DataSource source = new DataSource("C:\\Users\\Logan\\Desktop\\ML\\headbrain.arff");
				//this stores the values of the data in the dataset instances 
				Instances dataset = source.getDataSet();
				
					
				String[] opts = new String[]{ "-R", "1"};
				//create a Remove object to remove the non required attributes
				Remove remove = new Remove();
				//set the filter options
				remove.setOptions(opts);
				//pass the dataset to the filter
				remove.setInputFormat(dataset);
				//apply the filter to remove the first column which is the gender
				Instances newData = Filter.useFilter(dataset, remove);
				//applying the same filter again will remove the second column which is the age range
				remove.setInputFormat(newData);
				Instances newData1 = Filter.useFilter(newData, remove);
				

				
				ArffSaver saver = new ArffSaver();
				saver.setInstances(newData1);
				saver.setFile(new File("C:\\Users\\Logan\\Desktop\\ML\\headbraina.arff"));
				saver.writeBatch();
				System.out.println("The final dataset after removing non essential attributes is as follows");
				System.out.println(newData1);
				//load dataset
				DataSource sourcea = new DataSource("C:\\Users\\Logan\\Desktop\\ML\\headbraina.arff");
				Instances datasetsss = sourcea.getDataSet();	
				//set class index to the last attribute
				datasetsss.setClassIndex(datasetsss.numAttributes()-1);

				int seed = 1;
				int folds = 10;
				//  randomize data
				Random rand = new Random(seed);
				//create random dataset
				Instances randData = new Instances(datasetsss);
				randData.randomize(rand);
				//stratify	    
				if (randData.classAttribute().isNominal())
					randData.stratify(folds);

				// perform cross-validation	    	    
				for (int n = 0; n < folds; n++) {
					//Evaluation eval = new Evaluation(randData);
					//get the folds	      
					Instances train = randData.trainCV(folds, n);
					Instances test = randData.testCV(folds, n);	      
				
					//ArffSaver saver = new ArffSaver();
					saver.setInstances(train);
					System.out.println("No of folds done = " + (n+1));
					
					saver.setFile(new File("C:\\Users\\Logan\\Desktop\\ML\\trainheadbrain.arff"));
					saver.writeBatch();
					//if(n==9)
					//{System.out.println("Training set generated after the final fold is");
					//System.out.println(train);}
					
					ArffSaver saver1 = new ArffSaver();
					saver1.setInstances(test);
					saver1.setFile(new File("C:\\Users\\Logan\\Desktop\\ML\\testheadbrain.arff"));
					saver1.writeBatch();

				}

				//Random rand = new Random(1);
				//int folds = 10;
				double result=0;
				
				//load training dataset
				DataSource source1 = new DataSource("C:\\Users\\Logan\\Desktop\\ML\\trainheadbrain.arff");
				Instances trainDataset = source1.getDataSet();	
				//set class index to the last attribute
				trainDataset.setClassIndex(trainDataset.numAttributes()-1);
				//Evaluation eval = new Evaluation(trainDataset);
				//build model
				LinearRegression lr = new LinearRegression();
				lr.buildClassifier(trainDataset);
				
				System.out.println(lr);
				new Evaluation(trainDataset);
				

				//load new dataset
				DataSource source2 = new DataSource("C:\\Users\\Logan\\Desktop\\ML\\testheadbrain.arff");
				Instances testDataset = source2.getDataSet();	
				//set class index to the last attribute
				testDataset.setClassIndex(testDataset.numAttributes()-1);

				//loop through the new dataset and make predictions
				System.out.println("=====================================================");
				System.out.println("Actual Class, linear Predicted  ,    %error in value");
				for (int i = 0; i < testDataset.numInstances(); i++) {
					//get class double value for current instance
					double actualValue = testDataset.instance(i).classValue();

					//get Instance object of current instance
					Instance newInst = testDataset.instance(i);
					//call classifyInstance, which returns a double value for the class
					 double predreg = lr.classifyInstance(newInst);

					//System.out.println(actualValue+", "+predreg);
					double percenterro =Math.abs((actualValue-predreg)/actualValue)*100;
					System.out.println(actualValue+",       "+predreg+",     "+percenterro);
					
					result=result+percenterro;
					
					
					
				}
				result=100-(result/testDataset.numInstances());
				System.out.println(" \n The final accuracy of our model is "+ result +"%");
				
				Scanner sc=new Scanner(System.in);
				System.out.println(" \n Enter the value of headsize in cm^3");
				double headsize=sc.nextDouble();
				double brainweight=((0.2624*headsize)+329.1445);
				System.out.println(" \n The predicted value for weight of the brain is  "+ brainweight+" grams");
				//load dataset
						String datasetss = "C:\\Users\\Logan\\Desktop\\ML\\headbrain.arff";
						DataSource sourcess = new DataSource(dataset);
						//get instances object 
						Instances datass = source.getDataSet();
						// new instance of clusterer
						SimpleKMeans model = new SimpleKMeans();
						
						model.buildClusterer(datass);
						System.out.println(model);
						
						System.out.println("Here age range 1 signifies the youth of ages till 40 years of age and age range 2 signifies the later \n");
				
						if(brainweight>1282.000 && brainweight<=1310.000)
						{System.out.println("The following brain analysis resembles the brain of a youth");}
						else if(brainweight>=1262.000 && brainweight<=1289.000)
						{System.out.println("The following brain analysis resembles the brain of an older patient");}
						else
						{System.out.println("There is a high possiblity of deformility in the brain structure ");}

	}}

