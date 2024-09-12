Title: Vacations and Excursions App\
Purpose: To allow users to track their vacations and associated excursions, receive notifications, and share their vacations with others.

## APK generation instructions
1. Open in Android Studio
2. Go to Build, then click "Generate Signed App Bundle / APK…"
3. Choose the "APK" radio button, then click Next.
4. Click "Create new…" under "Key store path."
5. Choose a location for the "Key store path:" then input a password in the fields listed. Put your name in the Certificate. Then click "OK."
6. With the KeyStore and key alias created, click next.
7. Hold Shift and click on both debug and release, then click Create.
8. You will see a notification which states the builds are completed. 
9. Run and install the APK in an Android emulator.

## App usage instructions
1. Once running the app in the emulator, click the "Enter" button in the middle of the home screen.
2. This is the list of vacations screen.
3. Click the "plus" button in the bottom right to add a vacation. There is no max limit on vacations.
4. Here you will see the vacation details screen, which will list any associated excursions as well.
5. Enter the details including "title," the "accommodations," the "start" date, and the "end" date.
6. The start and end dates will use a date picker, automatically checking the date format.
7. The end date cannot be before the start date.
8. Once the vacation details have been entered, click the three dots in the top right, then click "Save vacation."
9. You will then see your list of vacations update on the vacation list screen.
10. Click the vacation you entered to return to the details of that vacation.
11. Here you can change the details of you vacation and save them again.
12. To add an excursion, click the "plus" button on the bottom right of the vacation details screen. There is not defined limit on excursions.
13. Enter your excursion details (title and date). The date will auto-format with the date-picker. Then click the three dots in the top right, then click "Save excursion" to save the excursion.
14. If the excursion date is before or after your vacation dates, a notification will pop up, and the excursion will not save.
15. Once the excursion saves, you will return to the vacation details screen automatically. You will see your associated excursions at the bottom.
16. To view the details of the excursion again, click on it on the Vacation details screen.
17. To be notified of your excursion, click the excursion, then click the three dots in the top right, then click "Notify." On the day of your excursion, you will receive a notification that your excursion is today.
18. To delete your excursion, click the three dots again, then click the "Delete excursion" button.
19. You will then return to the vacation details screen.
20. To be notified of your vacation start or end, click the three dots in the top right, and click "Notify." If it is the start date of your vacation, you will see a "starting" notification. If it is ending, you will see an "ending" notification.
21. To share the vacation information with others, click the three dots in the top right of the vacation details screen, then click "Share."
22. Then click your desired form of sharing, like Messages for example. It will ask you to choose a contact to share with. For testing purposes, enter a fake number like 1111111111 and hit enter. Then at the bottom you will see the contents of the message populated with the vacation details.
23. Return to the app. Then on the vacation details screen, you can change some of the details if you would like. Then click the three dots in the top right, and click "Save vacation."
24. Click on your vacation again to return to the details screen.
25. Click the three dots in the top right again and this time click "Delete vacation." If an excursion is still associated to the vacation, you will receive a notification that you "cannot delete a vacation with excursions." Otherwise, the vacation will delete.
