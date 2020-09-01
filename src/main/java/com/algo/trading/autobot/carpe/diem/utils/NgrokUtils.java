package com.algo.trading.autobot.carpe.diem.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public final class NgrokUtils
{
    private static Process tunnelProcess;

    private static final Logger LOGGER = Logger.getLogger(NgrokUtils.class.getName());

    private static final String NGROK_COMMAND =
        System.getProperty("user.dir") + File.separator + "tools" + File.separator + "ngrok.exe";

    private static final String NGROK_TUNNEL_COMMAND =
        NGROK_COMMAND + " http 127.0.0.1:5555 -host-header=127.0.0.1:5555";

    private NgrokUtils()
    {
    }

    public static void startTunnelingServer()
    {
        try {
            tunnelProcess = Runtime.getRuntime().exec(NGROK_TUNNEL_COMMAND);
            final InputStream inputStream = tunnelProcess.getInputStream();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            final String consoleOutput = bufferedReader.readLine();
            while (consoleOutput != null) {
                System.out.println(consoleOutput);
            }
        } catch (final IOException e) {
            LOGGER.info(e.getLocalizedMessage());
        }
    }

    public static void stopTunnelingServer()
    {
        if ((null != tunnelProcess) && tunnelProcess.isAlive()) {
            tunnelProcess.destroyForcibly();
        }
    }
}
