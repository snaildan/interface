package cn.gmw.api.meiyou.utils;

/**
 * @author 0-Vector
 * @date 2017年7月21日下午5:34:54
 */
public class LevenshteinDistance {

	
	/**
	 * @author 0-Vector
	 * @date 2017年2月14日下午4:37:40
	 * @param target
	 * @param other
	 * @return
	 */
    public static float getLevenshteinDistance(String target, String other) {
        char[] sa;
        int n;
        int p[]; //'previous' cost array, horizontally
        int d[]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d
        
          /*
             The difference between this impl. and the previous is that, rather
             than creating and retaining a matrix of size s.length()+1 by t.length()+1,
             we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
             is the 'current working' distance array that maintains the newest distance cost
             counts as we iterate through the characters of String s.  Each time we increment
             the index of String t we are comparing, d is copied to p, the second int[].  Doing so
             allows us to retain the previous cost counts as required by the algorithm (taking
             the minimum of the cost count to the left, up one, and diagonally up and to the left
             of the current cost count being calculated).  (Note that the arrays aren't really
             copied anymore, just switched...this is clearly much better than cloning an array
             or doing a System.arraycopy() each time  through the outer loop.)

             Effectively, the difference between the two implementations is this one does not
             cause an out of memory condition when calculating the LD over two very large strings.
           */

          sa = target.toCharArray();
          n = sa.length;
          p = new int[n+1]; 
          d = new int[n+1]; 
        
          final int m = other.length();
          if (n == 0 || m == 0) {
            if (n == m) {
              return 1;
            }
            else {
              return 0;
            }
          } 


          // indexes into strings s and t
          int i; // iterates through s
          int j; // iterates through t

          char t_j; // jth character of t

          int cost; // cost

          for (i = 0; i<=n; i++) {
              p[i] = i;
          }

          for (j = 1; j<=m; j++) {
              t_j = other.charAt(j-1);
              d[0] = j;

              for (i=1; i<=n; i++) {
                  cost = sa[i-1]==t_j ? 0 : 1;
                  // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                  d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
              }

              // copy current distance counts to 'previous row' distance counts
              _d = p;
              p = d;
              d = _d;
          }

          // our last action in the above loop was to switch d and p, so p now
          // actually has the most recent cost counts
          return 1.0f - ((float) p[n] / Math.max(other.length(), sa.length));
      }

}
