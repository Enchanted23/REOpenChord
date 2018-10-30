package cn.edu.ustc.slice;

import java.util.ArrayList;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple Merkle tree implementation. Intended to provide a verifiable
 * hash for the whole file in a cheap way.
 *
 * @author Qiang Xu
 */
class MerkleTree {

    private String root;

    private ArrayList<String> hashList;

    private MessageDigest hash;

    MerkleTree() {
        hashList = new ArrayList<String>();
		try {
			hash = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
		}
    }

    public void addHash(String hash) {
        hashList.add(hash);
    }

    public String getRoot() {
        ArrayList<String> origin = hashList;
        ArrayList<String> dest;
        do {
            int i = 0;
            dest = new ArrayList<String>();
            while (i < origin.size() - 1) {
                hash.update((origin.get(i) + origin.get(i+1)).getBytes());
                dest.add(hash.digest().toString());
                i += 2;
            }
            if (i < origin.size()) {
                hash.update(origin.get(i).getBytes());
                dest.add(hash.digest().toString());
            }
            origin = dest;
        } while(dest.size() != 1);

        return dest.get(0) + "-" + UUID.randomUUID().toString();
    }
}
