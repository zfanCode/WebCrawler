public interface Protocol
{
    int PORT = 4127;
    int CONN = 0;
    int SLAVE = 1;
    int CLIENT = 2;
    int RANDOM = 3;
    int USER = 4;
    int CATEGORY = 5;
    int NOJOB = 6;
    int JOB = 7;
    int OK = -100;
    int QUIT = -1;
    int DONE = -101;
    int BROKEN = -99;
}
