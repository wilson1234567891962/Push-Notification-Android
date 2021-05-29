package com.tutorials.eu.notificationssample

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val NOTIFICATION_ID = 888
    private lateinit var mNotificationManagerCompat: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNotificationManagerCompat = NotificationManagerCompat.from(this@MainActivity)

        val btnInboxStyleNotification = findViewById<Button>(R.id.btn_inbox_style)
        btnInboxStyleNotification.setOnClickListener(this@MainActivity)

        // TODO Step 2: Create an instance of Button and Assign the click event.
        // START
        val btnBigImageStyleNotification = findViewById<Button>(R.id.btn_big_image_style)
        btnBigImageStyleNotification.setOnClickListener(this@MainActivity)
        // END
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_inbox_style -> {
                generateInboxStyleNotification()
                return
            }

            // TODO Step 7: Assign a click event to button and call the generateInboxStyleNotification function.
            // START
            R.id.btn_big_image_style -> {
                generateBigPictureStyleNotification()
                return
            }
            // END
        }
    }

    /*
     * Generates a INBOX_STYLE Notification that supports both phone/tablet and wear. For devices
     * on API level 16 (4.1.x - Jelly Bean) and after, displays INBOX_STYLE. Otherwise, displays a
     * basic notification.
     */
    private fun generateInboxStyleNotification() {

        // Main steps for building a INBOX_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the INBOX_STYLE
        //      3. Set up main Intent for notification
        //      4. Build and issue the notification

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        val notificationChannelId: String =
            NotificationUtil().createInboxStyleNotificationChannel(this)

        // 2. Build the INBOX_STYLE.
        val inboxStyle =
            NotificationCompat.InboxStyle() // This title is slightly different than regular title, since I know INBOX_STYLE is
                // available.
                .setBigContentTitle(InboxStyleMockData.mBigContentTitle)
                .setSummaryText(InboxStyleMockData.mSummaryText)

        // Add each summary line of the new emails, you can add up to 5.
        for (summary in InboxStyleMockData.mIndividualEmailSummary()) {
            inboxStyle.addLine(summary)
        }

        // 3. Set up main Intent for notification that is the Activity that you want to launch when user tap on notification.
        val mainIntent = Intent(this, MainActivity::class.java)
        // Gets a PendingIntent containing the intent.
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 4. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. However, we don't need to update this notification later, so we
        // will not need to set a global builder for access to the notification later.
        val notificationCompatBuilder = NotificationCompat.Builder(
            applicationContext,
            notificationChannelId
        )

        notificationCompatBuilder // INBOX_STYLE sets title and content for API 16+ (4.1 and after) when the
            // notification is expanded.
            .setStyle(inboxStyle) // Title for API <16 (4.0 and below) devices and API 16+ (4.1 and after) when the
            // notification is collapsed.
            .setContentTitle(InboxStyleMockData.mContentTitle) // Content for API <24 (7.0 and below) devices and API 16+ (4.1 and after) when the
            // notification is collapsed.
            .setContentText(InboxStyleMockData.mContentText)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_person
                )
            )
            .setContentIntent(mainPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Set primary color (important for Wear 2.0 Notifications).
            .setColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.purple_500
                )
            ) // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            // .setGroupSummary(true)
            // .setGroup(GROUP_KEY_YOUR_NAME_HERE)
            // Sets large number at the right-hand side of the notification for API <24 devices.
            .setSubText(InboxStyleMockData.mNumberOfNewEmails.toString())
            .setCategory(Notification.CATEGORY_EMAIL) // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .setPriority(InboxStyleMockData.mPriority) // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
            .setVisibility(InboxStyleMockData.mChannelLockscreenVisibility)

        // If the phone is in "Do not disturb mode, the user will still be notified if
        // the sender(s) is starred as a favorite.
        for (name in InboxStyleMockData.mParticipants()) {
            notificationCompatBuilder.addPerson(name)
        }
        val notification = notificationCompatBuilder.build()
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    // TODO Step 5: Create a function to generate and launch the BigPictureStyle notification.
    // START
/*
     * Generates a BIG_PICTURE_STYLE Notification that supports both phone/tablet and wear. For
     * devices on API level 16 (4.1.x - Jelly Bean) and after, displays BIG_PICTURE_STYLE.
     * Otherwise, displays a basic notification.
     *
     * This example Notification is a social post.
     */
    private fun generateBigPictureStyleNotification() {

        // Main steps for building a BIG_PICTURE_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_PICTURE_STYLE
        //      3. Set up main Intent for notification
        //      4. Set up RemoteInput, so users can input (keyboard and voice) from notification
        //      5. Build and issue the notification

        // 0. Get your data (everything unique per Notification).

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        val notificationChannelId: String =
            NotificationUtil().createBigPictureStyleNotificationChannel(this@MainActivity)

        // 2. Build the BIG_PICTURE_STYLE.
        val bigPictureStyle =
            NotificationCompat.BigPictureStyle() // Provides the bitmap for the BigPicture notification.
                .bigPicture(
                    BitmapFactory.decodeResource(
                        resources,
                        BigPictureStyleMockData.mBigImage
                    )
                ) // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle(BigPictureStyleMockData.mBigContentTitle) // Summary line after the detail section in the big form of the template.
                .setSummaryText(BigPictureStyleMockData.mSummaryText)

        // 3. Set up main Intent for notification.
        val mainIntent = Intent(this, MainActivity::class.java)
        // Gets a PendingIntent containing the mainIntent.
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for a comment on the post.
        val notificationCompatBuilder = NotificationCompat.Builder(
            applicationContext, notificationChannelId
        )

        notificationCompatBuilder // BIG_PICTURE_STYLE sets title and content for API 16 (4.1 and after).
            .setStyle(bigPictureStyle) // Title for API <16 (4.0 and below) devices.
            .setContentTitle(BigPictureStyleMockData.mContentTitle) // Content for API <24 (7.0 and below) devices.
            .setContentText(BigPictureStyleMockData.mContentText)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_person
                )
            )
            .setContentIntent(mainPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Set primary color (important for Wear 2.0 Notifications).
            .setColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.purple_500
                )
            ) // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            // .setGroupSummary(true)
            // .setGroup(GROUP_KEY_YOUR_NAME_HERE)
            .setSubText(1.toString())
            .setCategory(Notification.CATEGORY_SOCIAL) // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .setPriority(BigPictureStyleMockData.mPriority) // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
            .setVisibility(BigPictureStyleMockData.mChannelLockscreenVisibility)

        // If the phone is in "Do not disturb mode, the user will still be notified if
        // the sender(s) is starred as a favorite.
        for (name in BigPictureStyleMockData.mParticipants()) {
            notificationCompatBuilder.addPerson(name)
        }

        val notification = notificationCompatBuilder.build()

        // TODO Step 6: Notify the user using notification id and Notification builder with notification manager.
        // START
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification)
        // END
    }
    // END
}