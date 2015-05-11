import java.io.*;
import java.util.ArrayList;

/* use fourier technique to generate PSD for a sample wavfile */

public class ReadExample {

	public static void main(String[] args) {
		try {
			WavFile wavFile = WavFile.openWavFile(new File(args[0]));

			// Display information about the wav file
			wavFile.display();

			// Get the no of audio channels in wav file - I'm using one channel
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 1 frames
			double[] buffer = new double[1 * numChannels];
			ArrayList<Double> values = new ArrayList<Double>();

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do {
				/* Read frames into arraylist */
				framesRead = wavFile.readFrames(buffer, 1);

				for (int s=0 ; s<framesRead * numChannels ; s=s+4) {
					values.add(buffer[s]);
				}
			}
			while (framesRead != 0);

			// launch into DFT
			int N = values.size();
			double twoPikOnN;
			double twoPijkOnN; 
			double twoPiOnN = 2 * Math.PI / N;

			double r_data[] = new double[N];
			double i_data[] = new double[N];
			double psd[] = new double[N];
			for(int k=0; k < N; k++){
				twoPikOnN = twoPiOnN * k;

				for(int j = 0; j < N; j++) {
					twoPijkOnN = twoPikOnN * j;
					r_data[k] += values.get(j) * Math.cos( twoPijkOnN );
					i_data[k] -= values.get(j) * Math.sin( twoPijkOnN );
				}
				r_data[k] /= N;
				i_data[k] /= N; 
	
				psd[k] = r_data[k] * r_data[k] + i_data[k] * i_data[k];
				System.out.printf("\n%d,%.06f",k,psd[k]);
			}
			wavFile.close();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}
