package speechtotextandtexttospeechchatbot;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SpeechToTextAndTextToSpeechChatbot
{

    private static JSlider volumeSlider;
    private static JSlider speedSlider;

    public static void main(String[] args)
    {
        // Create and display the GUI for adjusting volume and speed
        createAndShowGUI();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running)
        {
            try
            {
                System.out.println("Choose an option: ");
                System.out.println("1. Text to Speech");
                System.out.println("2. Chatbot");
                System.out.println("3. All available voices");
                System.out.println("4. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice)
                {
                    case 1:
                        System.out.println("Enter text to convert to speech:");
                        String text = scanner.nextLine();
                        textToSpeech(text);
                        break;
                    case 2:
                        chatbot();
                        break;
                    case 3:
                        Voices();
                        break;
                    case 4:
                        running = false;  // Exit the loop
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e)
            {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the buffer
            }
        }
        scanner.close();
    }
//----------------------------------------------------------------------------//
    //Create GUI  for Volume and Speed Sliders

    private static void createAndShowGUI()
    {
        //Create JFrame
        JFrame frame = new JFrame("Chatbot Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));

        // Volume slider
        volumeSlider = new JSlider(0, 10, 5); // Min: 0, Max: 10, Initial: 5
        volumeSlider.setMajorTickSpacing(2);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        frame.add(createLabeledPanel("Volume", volumeSlider));

        // Speed slider
        speedSlider = new JSlider(50, 200, 120); // Min: 50, Max: 200, Initial: 120
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setMinorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        frame.add(createLabeledPanel("Speech Speed", speedSlider));

        frame.pack();
        frame.setVisible(true);
    }
//----------------------------------------------------------------------------//

    private static JPanel createLabeledPanel(String label, JSlider slider)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }
//----------------------------------------------------------------------------//

    public static void Voices()
    {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory,"
                + "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();

        System.out.println("Available voices:");
        for (Voice v : voices)
        {
            System.out.println(v.getName());
        }
    }
//----------------------------------------------------------------------------//

    public static void textToSpeech(String text)
    {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory,"
                + "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");

        // Fetching the voice to be used from the TTS engine
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice("kevin16");

        if (voice != null)
        {
            voice.allocate();

            // Set parameters based on slider values
            voice.setRate(speedSlider.getValue());
            voice.setPitch(100); // Neutral pitch
            voice.setVolume(volumeSlider.getValue());

            voice.speak(text);
            voice.deallocate();
        } else
        {
            System.err.println("Voice not found.");
        }
    }
//----------------------------------------------------------------------------//

    public static void chatbot()
    {
        List<String[]> responses = new ArrayList<>();
        responses.add(new String[]
        {
            "hello", "Hi there!"
        });
        responses.add(new String[]
        {
            "bye", "Goodbye!"
        });

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start chatting with the bot (type 'exit' to end):");

        while (true)
        {
            System.out.print("You: ");
            String userInput = scanner.nextLine().toLowerCase();
            if (userInput.equals("exit"))
            {
                System.out.println("Bot: Goodbye!");
                break;
            }

            String response = "I'm not sure how to respond to that.";
            for (String[] pair : responses)
            {
                if (pair[0].equals(userInput))
                {
                    response = pair[1];
                    break;
                }
            }

            System.out.println("Bot: " + response);
            // Allows Chatbot to provide auditory responses
            textToSpeech(response);
        }
    }
}
//-------------------------END OF FILE----------------------------------------//
