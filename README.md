# CS Research Finder
## Team Information
### Team Members
- Nabiha Tariq
- Yusyra Hossain
- Iman Imran
- Dana Younes
- Jasmine Bui
---

## Project Description
CS Research Finder helps students find computer science research opportunities. Users can browse professors by their research area/topic and locations and view their current works. Users can create an account to track which professors they have reached out through the contact information available on the professor page. Tentatively cold email template and summaries. 

### Walkthrough 
First user signs in or signs up with their information and areas of interest. Then, we get a landing page with a two search filters on location and topics. After loging in, the platform will recommend one research paper based on the user's profile interests. User selects what they want, searches, and gets a list of professors who have research in those areas. The user will then click on professor names to find research papers of interest. In the professor profile, there will be contact information for the user to cold email. Once the user has contacted the professor, they will click a button to signal they've contacted the professor and that is stored in the database. On each professor profile, there will be a cold email template personalized for each user that relates to the professor's work and the user's interest.

The scope of the project will be institutions in Canada, mainly in the GTA and other larger institutions across Canada.

---
## User Stories
### As a user, I want to be able to create an account and login with my interests so that I can receive personalized research papers and cold emails. 
### As a user, I want to be able to receive a ranked list of professors who match my research interests and preferred location/institution, so that I know who I can contact for opportunities.
### As a user, I want to be able to receive a recommended research paper that is catered to my interests so that I can explore this area more. 
### As a user, I want to be able to save and organize research papers that are of interest to me so that I can keep track of what papers I enjoyed.
### As a user, I want to be able to track what professors I have contacted and their reply status, so that I can manage my outreach. 
### As a user, I want to be able to search for a professor, an area of interest, or institution so that I can find professors and papers who align with my interests. 
### As a user, I want to be able to click on professor names in my results so that I can see a summary of their research and papers along with a link to the institution they are working at. 
### As a user, I want to be able to receive a cold email template while I’m visiting a professor page that is personalized with my interests and the professor’s area of research.
---
## Use Cases
### Use Case 1: Signing up/Creating an account 
#### Primary Actor:
User 
#### Preconditions:
- User has access to the application
- User doesn't have an account if attempting to create a new account
#### Trigger:
The user clicks on a button to create an account or a button to login
#### Success Outcome:
- User has successfully created an account or has logged into the application, launching a home page on application
- Unsuccessful login or creation of account will lead to a popup error message prompting user to try again
- User's interests and information are saved to database
- The user can receive personalized papers and cold emails based on their information
#### Main Success Scenario:
1. User opens application
2. User selects option to create an account
3. System prompts user to enter their information and interests
4. User clicks submit button
5. System validates submitted information to ensure no errors
6. System creates user account and saves information to database
7. System displays message that account was created successfully
8. User logs in with their account name and password
9. System verifies the password and grants access to application
10. System launches home page where user can view a personalized paper, personalized suggestions for professors to contact, and search bar.
#### Extensions:
##### 4a: User leaves fields empty
1. System will display message with fields that still need to filled out by user
2. Use case continues at step 4
##### 5a: Email entered is invalid and not in use (subject to change if we don't want to collect emails bc of privacy)
1. System will prompt user to reenter a correct email
2. Use case continues at step 5
##### 5b: Email entered is already in database
1. System will inform user that they already have an account
2. System will prompt user to login
3. Use case continues at step 8
##### 8a: User enters wrong credentials
1. System displays message that credentials are incorrect 
2. System prompts user to reenter credentials
3. Use case continues at step 8


### Use Case 2: View a ranked list of Matching Professors
#### Primary Actor: 
User that already has an account
#### Preconditions: 
- The user is logged in.
- The user has just logged in, or has returned to the landing page while logged in.
- The user’s profile contains at least one research interest.
#### Trigger: 
The user arrives at the landing page while logged in.
#### Success Outcome: 
The user sees a list of professors ordered from best match to weakest match, based on their profile, each with enough information to decide whether to view their profile.
#### Main success scenario: 
1. The user logs in, and arrives at the landing page.
2. The system finds professors whose research areas match the user’s profile interests and preferred location/institution.
3. The system ranks the matching professors by how well they fit.
4. The system shows the ranked list, along with the criteria used and the location and topic filters. Each entry shows the professor’s name, institution, and the research areas that matched.
5. The user reviews the list.
#### Extensions: 
##### 2a. The user’s profile has no preferred location or institution.
1. The system ranks using research interests only.
2. The system tells the user that adding a location or institution would improve the results.
3. The use case continues at step 4.
##### 4a. No professors match the user’s profile.
1. The system tells the user that no matches were found.
2. The system suggests adding more interests or location.
3. The use case ends.
##### 4b. Many professors match.
1. The system shows the best matches first.
2. The user can ask to see more results.




### Use Case 3
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Nabiha | Use Case #1 - Login |
| Yusyra | Use Case #2 - Ranked list |
| Iman | Use Case #5 - Tracking Contact |
| Jasmine | Use Case #4 - Saving Papers|
| Dana | Use Case #3 - Recommended Papers|
---
## Entities
### Entity 1
### Entity 2
(Add additional entities as needed.)
---
## API Usage
**API Name:**
**Documentation:** [Link]
**How it is used in the program:**
---
## Design Diagrams (Optional)
### UML Class Diagram
### Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
---
## Notes
[Any additional project information.]
