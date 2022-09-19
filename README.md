# Silly Twitch stream game for cruciverbalists

Because we needed another thing to waste lots of time doing :)

## Database

I wrote this using MariaDB (it's very easy for me to set up in Debian), but
there's really nothing requiring MariaDB/MySQL.

I have provided a SQL script in ``resources/xwdgame_tables_create.sql`` which
will create the three database tables needed. They ought to be trivially easy
to convert to your RDBMS of choice.

Edit ``resources/META-INF/persistence.xml`` as appropriate for your environment
so that Hibernate can find the database.

## Twitch OAuth key

Google for "Twitch bot OAUTH token" or similar and follow the instructions in
one of the 73 umptillion websites/videos out there showing you how to create
one.

Place the token in ssta.xwdgame.bot.Private.java in the ``OAUTH_KEY`` constant.

You may wish to change the channel your bot connects to in
``ssta.xwdgame.main.Main`` - I'll be a little unhappy if random bots start to
show up in my channel :)

Otherwise, it all ought to be fairly self-explanatory.

Pull requests will be looked at as and when I have time to do so :)
