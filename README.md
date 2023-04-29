# Art-AI
All code for the project falls into the following sections

Login Activity - User can login to the application through a known user email and password (Connected to Firebase)

Register Activity - User can register an account with "Whats my Art Worth" in order to save information for later use (Connected to Firebase)

Main Activity - This activity is the fragment manager and controls the Home, Favorites, and Setting Fragments. 

Home Fragment - This fragment contains two buttons, the first takes the user to the "Take Picture Activity", and the second button will allow the user to upload a Picture to fire
Firebase Storage. This page contains a imageview which will allow the user to review the image before uploading. 

Favorites Fragment - This section will be the main focus for ECEN 404, adding a recyclerview and the ability to retrieve the last 10 pictures taken from Firebase Storage once the art has been appraised.

Settings Fragment- This fragment contains a short tutorial and a logout button that is connected to Firebase, when pressed it will log the user out of the application 
and send them back to the login page with a message saying that they were successfully logged out. 

Take Picture activity - This activity is used to take the picture for the user. It was created using CameraX which is a jetpack add-on to Camera2. Connected to this 
activity is the constraints file which gives the path of where the picture is stored local on the phone. First time using the CameraX requires the user to allow access
to the phones camera, if the user does not give the application permission to use the camera the application will return to the home fragment and let the user know that
we permission was deined and we can not access the camera.

Connection Manager - The connection manager is constaintly checking to see if the user has connection to the internet. This is done by checking celluar and wifi, if both
of these services are lost a message will be displayed letting the user know that we are not connected to the internet and the user will not be able to upload any pictures or login to the application.

All XML files are the layout of each activity/fragment of the application showing what is being displayed and the structure of each element on the activity/fragment.
