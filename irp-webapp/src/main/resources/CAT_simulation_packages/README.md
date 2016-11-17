# CAT Simulation Packages
 The vendor will initiate a session by selecting to download a simulation package from IRP.
 This simulation package will include all of the information required of the vendor’s test delivery system (TDS) to run a CAT simulation.  
 The package consists of fake student data for 1000 students.
 Each student is generated using a population score, and the target system should ideally score similarly. 
 
 ## Package Contents
  1. An item pool: a list of all items available for administration and the items’ associated metadata, including: 
    a) unique item identifiers
    b) item response theory (IRT) scoring parameters (from item calibration)
    c) content-based classifications (i.e., by subject, claim, target)
    d) classification by cognitive complexity (depth of knowledge)
    e) identification of item sets sharing a common stimulus
  2. Test blueprints specifying the number of items with particular characteristics and/or content to be administered per test instance
    a) https://www.smarterbalanced.org/wp-content/uploads/2015/08/Mathematics_Blueprint.pdf
    b) https://www.smarterbalanced.org/wp-content/uploads/2015/08/ELA_Blueprint.pdf
  3. Fake student data, consisting of unique student identifiers and scores for all items in the item pool (generated based on the parameters of each item and the simulated student’s true score).
	- Vendors will not have the simulated students’ true scores.

- Data for ELA Grade 11 and 3 Simulation packages are created in their respective folders: `./ELAG11` and `./ELAG3`

## Creating New Simulation Packages

### Distributed Files
  1. item pool - consisting of all item that are available for the current simulation
  2. Test blueprints - links to the blueprints from smarterbalanced
  3. Fake student data - consists of answers for each 1000 students to all the item pool questions 

### Internal Files
True Theta values are the only file that needs to be create but not distributed to client.
The format is a csv file with headers:
    "student ID","theta value"
which represents the true score for a student.

  

