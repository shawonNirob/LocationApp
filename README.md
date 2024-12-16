##Location App

###Introduction
The Location App is an Android application that:
1. Tracks the user's current location using GPS.
2. Displays the latitude and longitude on the screen.
3. Shows the user's location on a Google Map with a marker.
4. Updates location every 10 minutes automatically.

###Main Features
1. Permission Handling:
    Requests Location Permission at runtime.
    Guides the user to settings if permission is denied permanently.

2. Location Tracking:
    Fetches location using FusedLocationProviderClient.
    Displays `Latitude` and `Longitude` in a TextView.
    Updates the map marker and camera position to the current location.

3. Google Maps Integration:
    Shows user location on a map with a marker.
    Automatically centers the map on the current location.

4. Periodic Updates:
    Updates the user's location every 10 minutes using a Handler.

## **Tools & Technologies**
- **Android Studio**: Development IDE.
- **Java**: Programming language for app logic.
- **MVVM Architecture**: Clean separation of concerns.
- **Google Maps SDK**: For displaying the map.
- **FusedLocationProviderClient**: For fetching location data.
