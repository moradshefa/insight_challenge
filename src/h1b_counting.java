/**
 * Created by momo on 11/3/18.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


class Node implements Comparable<Node>{
    public String val;
    public int accepted = 1;

    public Node(String s) {
        this.val = s;
    }

    public void add_accepted() {
        accepted++;
    }

    @Override
    public int compareTo(Node n2) {
        // First sort bast one number of accepted in decreasing order
        int acceptedRateComparitor = this.accepted - n2.accepted;
        if (acceptedRateComparitor != 0)
            return -acceptedRateComparitor;

        // if number of accepted was equal sort in ascending order of string
        return this.val.compareTo(n2.val);
    }
}

public class h1b_counting {
    public static void main(String [ ] args)
    {
        try {
            // Read first line of the file, header
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            String line = in.readLine();
            String[] lineSplit = line.split(";", -1);
            int length = lineSplit.length;

            // Find indices of fields we are interested in
            int statusI = -1;
            int titleI = -1;
            int stateI = -1;
            for (int i=0; i<length; i++) {
                if (lineSplit[i].compareTo("CASE_STATUS") == 0) statusI=i;
                if (lineSplit[i].compareTo("SOC_NAME") == 0) titleI=i;
                if (lineSplit[i].compareTo("WORKSITE_STATE") == 0) stateI=i;
            }

            if (statusI < 0) throw new Exception("Could not find status index");
            if (titleI < 0) throw new Exception("Could not find job title index");
            if (stateI < 0) throw new Exception("Could not find state index");

            // HashMap counter
            HashMap<String, Node> states = new HashMap<>();
            HashMap<String, Node> occupations = new HashMap<>();


            int totalCertified = 0;

            String state, title, status;
            line = in.readLine();

            while (line!=null) {
                lineSplit = line.split(";", -1);

                // if for some reason should a line not have the index of the field we continue
                if (lineSplit.length <= statusI || lineSplit.length <= titleI || lineSplit.length <= stateI) {
                    if (line != null) line = in.readLine();
                    continue;
                }
                state = lineSplit[stateI];
                title = lineSplit[titleI];
                status = lineSplit[statusI];

                if (status.compareTo("CERTIFIED") == 0) {
                    // If the maps contain the title or state, increment by one, else put a new one in
                    // with count 1
                    if (states.containsKey(state)) states.get(state).add_accepted();
                    else states.put(state, new Node(state));

                    if (occupations.containsKey(title)) occupations.get(title).add_accepted();
                    else occupations.put(title, new Node(title));

                    totalCertified++;
                }
                line = in.readLine();
            }
            // Sort the maps
            ArrayList<Node> states_ = new ArrayList<>(states.values());
            ArrayList<Node> occupations_ = new ArrayList<>(occupations.values());
            Collections.sort(states_);
            Collections.sort(occupations_);

            // Write the first 10 elements
            FileWriter fileWriter = new FileWriter(args[2]);
            fileWriter.write("TOP_STATES;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE\n");
            for (int k = 0; k < Math.min(states_.size(), 10); k++)
                fileWriter.write(states_.get(k).val + ';' + states_.get(k).accepted+ ';' + String.format("%.1f", 100*(float)states_.get(k).accepted/totalCertified)+"%\n" );
            fileWriter.close();

            fileWriter = new FileWriter(args[1]);
            fileWriter.write("TOP_OCCUPATIONS;NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE\n");
            for (int k = 0; k < Math.min(occupations_.size(), 10); k++)
                fileWriter.write(occupations_.get(k).val.replace("\"", "") + ';' + occupations_.get(k).accepted + ';' + String.format("%.1f", 100*(float)occupations_.get(k).accepted/totalCertified) +"%\n");
            fileWriter.close();
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }
}
