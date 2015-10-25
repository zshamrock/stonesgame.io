### How to run
Just do:

- `./gradlew run` on Linux/Mac
- `gradlew.bat run` on Windows

Then the main application is available at http://localhost:58080/ and the admin interface accessible at http://localhost:58081/.

Ask another user to join the game as well, and you are ready to go!

### Available environment variables
<table>
    <tr>
        <th>Name (default value)</th>
        <th>Purpose</th>
        <th>Possible values</th>
    </tr>
    <tr>
        <td>PORT (58080)</td>
        <td>Port on which the application is running.</td>
        <td>Any available port. In case of Heroku, except port 80, 
            as it binds the application on the configured PORT and then forward requrest from :80 to the application.
        </td>
    </tr>
    <tr>
        <td>BOTS_ENABLED (true)</td>
        <td>Enable or disable bots support.</td>
        <td>true | false</td>
    </tr>
    <tr>
        <td>BOTS_SCHEDULED_PERIOD_IN_SECONDS (30)</td>
        <td>Period in secods of a new bot to be added to the players queue, so to be available to join the game.</td>
        <td>Any positive number greater or equal 10. Reasonable values are between 10 and 30 seconds.</td>
    </tr>
    <tr>
        <td>BOTS_POOL_SIZE (10)</td>
        <td>Maximum number of bots avaialable in the application.</td>
        <td>Any positive number less or equal 100. Reasonable values are between 5 and 100.</td>
    </tr>
</table>

### License
The MIT License (MIT)

Copyright (c) 2015 Aliaksandr Kazlou

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
